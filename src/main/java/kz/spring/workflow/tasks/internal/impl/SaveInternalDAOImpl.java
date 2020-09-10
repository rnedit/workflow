package kz.spring.workflow.tasks.internal.impl;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.configuration.Numerator;
import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import kz.spring.workflow.repository.Impl.NumeratorDALImpl;
import kz.spring.workflow.tasks.internal.SaveInternalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveInternalDAOImpl implements SaveInternalDAO {

    @Autowired
    NumeratorDALImpl numeratorDAL;

    @Autowired
    InternalDALImpl internalDAL;

    @Override
    public Boolean setNumberAndSave(EventQueue eventQueue) {
        if (eventQueue==null) {
            throw new IllegalArgumentException("EventQueue eventQueue cannot be null");
        }
        Internal internal = internalDAL.getInternal(eventQueue.getDocumentId());
        internal.setNumber(this.generateNumber(internal));
        Internal updateInternal = internalDAL.saveInternal(internal);
        if (updateInternal==null) {
            throw new IllegalArgumentException("Internal updateInternal cannot be null");
        }
        return true;
    }


    private String generateNumber(Internal internal) {
        if (internal==null) {
            throw new IllegalArgumentException("Internal internal cannot be null");
        }

        Profile creationProfile = internal.getСreatorProfile();
        if (creationProfile==null) {
            creationProfile = new Profile("Без профайла");
            creationProfile.setSuffix("");
        }

        String suffix = creationProfile.getSuffix();

        if (suffix==null && suffix.isBlank()) {
            suffix="not";
        }
        final Numerator numerator = numeratorDAL.getNumeratorBySuffix(suffix);

        numerator.setNumber(numerator.getNumber()+1);

        Numerator newNumerator =  numeratorDAL.saveNumerator(numerator);

        String postfix = ( newNumerator.getPostfix()==null && newNumerator.getPostfix().isBlank() )?"":newNumerator.getPostfix();
        String number = new StringBuilder()
                .append(newNumerator.getPrefix())
                .append(newNumerator.getPrefix().isBlank()?"":"/")
                .append(newNumerator.getNumber())
                .append(postfix).toString();
        return number;
    }
}
