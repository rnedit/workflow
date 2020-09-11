package kz.spring.workflow.service.DAL;

import kz.spring.workflow.service.util.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AttachmentService {
    void deleteAttachment(String id);
    String addAttachment(String name, String type, MultipartFile file) throws IOException;
    Attachment getAttachment(String id) throws IllegalStateException, IOException;

}


