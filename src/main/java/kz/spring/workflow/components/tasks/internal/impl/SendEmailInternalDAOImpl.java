package kz.spring.workflow.components.tasks.internal.impl;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.eventqueue.EventQueue;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import kz.spring.workflow.service.EmailServiceImpl;
import kz.spring.workflow.service.util.EmailFields;
import kz.spring.workflow.components.tasks.internal.SendEmailInternalDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SendEmailInternalDAOImpl implements SendEmailInternalDAO {

    final
    private EmailServiceImpl emailService;

    final
    private InternalDALImpl internalDAL;

    @Autowired
    public SendEmailInternalDAOImpl(EmailServiceImpl emailService, InternalDALImpl internalDAL) {
        this.emailService = emailService;
        this.internalDAL = internalDAL;
    }

    @Override
    public Boolean Execute(EventQueue eventQueue) {
        if (!send(eventQueue)){
            return false;
        }
        return true;
    }

    @Override
    public Boolean send(EventQueue eventQueue) {
        Internal internal = internalDAL.getInternal(eventQueue.getDocumentId());

        Set<Profile> allReadersProfiles = internal.getProfilesAllReaders();
        List<String> Emails = new ArrayList<>();
        allReadersProfiles.forEach(p->{
            Emails.add(p.getUser().getEmail());
        });
        EmailFields emailFields = new EmailFields();
        emailFields.setTo(Emails.toArray(new String[Emails.size()]));
        emailFields.setSubject("Новый документ");

        Map<String, Object> modal = new HashMap<>();
        modal.put("msg","Поступил новый документ!" +
                "<br>" +
                "Краткое содержание документа: "+internal.getSubject());

        return emailService.modalAndSend(emailFields,"default.ftl", modal);
    }
}
