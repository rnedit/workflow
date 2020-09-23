package kz.spring.workflow.components.tasks.internal.impl;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.configuration.Numerator;
import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.events.eventHandler.EventQueueHandlerPublisher;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import kz.spring.workflow.repository.Impl.NumeratorDALImpl;
import kz.spring.workflow.components.tasks.internal.SaveInternalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static kz.spring.workflow.components.tasks.types.InternalTaskType.TASK_SENDEMAILINTERNAL;

@Service
public class SaveInternalDAOImpl implements SaveInternalDAO {

    final
    private NumeratorDALImpl numeratorDAL;

    final
    private InternalDALImpl internalDAL;

    final
    private SendEmailInternalDAOImpl sendEmailInternalDAO;

    final
    private EventQueueHandlerPublisher eventQueueHandlerPublisher;

    @Autowired
    public SaveInternalDAOImpl(NumeratorDALImpl numeratorDAL, InternalDALImpl internalDAL,
                               SendEmailInternalDAOImpl sendEmailInternalDAO,
                               EventQueueHandlerPublisher eventQueueHandlerPublisher) {
        this.numeratorDAL = numeratorDAL;
        this.internalDAL = internalDAL;
        this.sendEmailInternalDAO = sendEmailInternalDAO;
        this.eventQueueHandlerPublisher = eventQueueHandlerPublisher;
    }

    @Override
    public Boolean setNumberAndSave(EventQueue eventQueue) {
        if (eventQueue == null) {
            throw new IllegalArgumentException("EventQueue eventQueue cannot be null");
        }
        Internal internal = internalDAL.getInternal(eventQueue.getDocumentId());
        internal.setNumber(this.generateNumber(internal));
        Internal newInternal = internalDAL.saveInternal(internal);
        if (newInternal == null) {
            throw new IllegalArgumentException("Internal cannot be saved");
        }
        return true;
    }

    @Override
    public Boolean Execute(EventQueue eventQueue) {

        if (!setNumberAndSave(eventQueue)) {
            return false;
        }

        Internal internal = internalDAL.getInternal(eventQueue.getDocumentId());

        switch (internal.getTypeAgreement()) {
            case 0 -> sendEmailQueue(eventQueue); //без согласования
            case 1 -> sendEmailQueue(eventQueue); //Параллельное
            case 2 -> sendEmailQueue(eventQueue); //Последовательное
            default -> throw new IllegalArgumentException("SaveInternalDAOImpl internal.getTypeAgreement()=" + internal.getTypeAgreement() + " Seriously?!");
        }
        return true;
    }

    private void sendEmailQueue(EventQueue eventQueue) {
        EventQueue eQ = new EventQueue();
        eQ.setCreatorUserId(eventQueue.getCreatorUserId());
        eQ.setDocumentId(eventQueue.getDocumentId());
        eQ.setTaskName(TASK_SENDEMAILINTERNAL);
        eventQueueHandlerPublisher.doEventHandler(eQ);
    }

    private String generateNumber(Internal internal) {
        if (internal == null) {
            throw new IllegalArgumentException("Internal internal cannot be null");
        }

        Profile creationProfile = internal.getCreatorProfile();
        if (creationProfile == null) {
            creationProfile = new Profile("Без профайла");
            creationProfile.setSuffix("");
        }

        String suffix = creationProfile.getSuffix();

        if (suffix == null && suffix.isBlank()) {
            suffix = "not";
        }
        final Numerator numerator = numeratorDAL.getNumeratorBySuffix(suffix);

        numerator.setNumber(numerator.getNumber() + 1);

        Numerator newNumerator = numeratorDAL.saveNumerator(numerator);

        String postfix = (newNumerator.getPostfix() == null && newNumerator.getPostfix().isBlank()) ? "" : newNumerator.getPostfix();
        String number = new StringBuilder()
                .append(newNumerator.getPrefix())
                .append(newNumerator.getPrefix().isBlank() ? "" : "/")
                .append(newNumerator.getNumber())
                .append(postfix).toString();
        return number;
    }
}
