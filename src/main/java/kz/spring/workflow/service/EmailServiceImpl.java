package kz.spring.workflow.service;

import kz.spring.workflow.service.util.EmailFields;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

//https://www.baeldung.com/spring-email

@Service
public class EmailServiceImpl implements EmailService{

    @Value("${emailFrom}")
    String emailFrom;

    @Value("${hostname}")
    String hostname;

    final
    private FreemarkerTemplate freemarkerTemplate;

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender, FreemarkerTemplate freemarkerTemplate) {
        this.emailSender = emailSender;
        this.freemarkerTemplate = freemarkerTemplate;
    }

    @Override
    public Boolean modalAndSend(EmailFields emailFields, String templateFTL, Map<String, Object> modal) {

        SimpleDateFormat formatTimeText = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        modal.put("hostname", hostname);
        modal.put("time", formatTimeText.format(cal.getTime()));
        String html = freemarkerTemplate.mapTemplateFreemarker(templateFTL, modal);
        emailFields.setBody(html);

        return sendMessageWithAttachment(emailFields);
    }

    @Override
    public Boolean sendSimpleMessage(String[] to,
                                     String subject,
                                     String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
            return true;
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
            return false;
        }

    }

    @SneakyThrows
    @Override
    public Boolean sendMessageWithAttachment(EmailFields emailFields) {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(emailFrom);
        helper.setTo(emailFields.getTo());
        helper.setSubject(emailFields.getSubject());
        helper.setText(emailFields.getBody(), true);

        if (emailFields.getPathToAttachment() != null) {
            FileSystemResource file
                    = new FileSystemResource(new File(emailFields.getPathToAttachment()));
            helper.addAttachment("Invoice", file);
        }

        try {
            emailSender.send(message);
            return true;
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    public boolean checkMX(String domain) {

        try {
            Hashtable env = new Hashtable();
            env.put("java.naming.factory.initial",
                    "com.sun.jndi.dns.DnsContextFactory");
            env.put("com.sun.jndi.dns.read.timeout", "5000");
            DirContext ictx = new InitialDirContext( env );
            Attributes attrs = ictx.getAttributes
                    ( domain, new String[] { "MX" });
            Attribute attr = attrs.get( "MX" );
            if (( attr == null ) || ( attr.size() == 0 )) {
                attrs = ictx.getAttributes( domain, new String[] { "A" });
                attr = attrs.get( "A" );
                if( attr == null )
                    throw new NamingException
                            ( "No match for name "+domain );
            }
        } catch (NamingException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean isCyrillic(String aEmailAddress){
        for(int i = 0; i < aEmailAddress.length(); i++) {
            if(Character.UnicodeBlock.of(aEmailAddress.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return true;
            }
        }
        return false;
    }
}
