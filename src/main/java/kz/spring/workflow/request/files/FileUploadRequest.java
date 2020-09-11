package kz.spring.workflow.request.files;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileUploadRequest {

    List<InfoFile> infoFiles;
    List<MultipartFile> multipartFiles;

}
