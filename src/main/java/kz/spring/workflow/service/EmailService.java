package kz.spring.workflow.service;

import kz.spring.workflow.service.util.EmailFields;

import java.util.Map;

public interface EmailService {
    Boolean sendSimpleMessage(String[] to, String subject, String text);
    Boolean sendMessageWithAttachment(EmailFields emailFields);
    Boolean modalAndSend(EmailFields emailFields, String templateFTL, Map<String, Object> modal);
}
