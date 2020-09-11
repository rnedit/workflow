package kz.spring.workflow.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kz.spring.workflow.request.files.InfoFile;
import kz.spring.workflow.service.AttachmentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR')")
@RequestMapping("/api/file")
public class FileController {

    final
    private AttachmentServiceImpl attachmentService;

    public FileController(AttachmentServiceImpl attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> upload(
            @RequestParam("files") final MultipartFile[] files,
            @RequestParam("infoFiles") final Object infoFiles
                                         ) {

        List<MultipartFile> multipartFiles = Arrays.asList(files);

        List<InfoFile> infoFileArrayList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (infoFiles instanceof String[]) {
            String[] ls = (String[]) infoFiles;
            List<String> listInfoFiles = Arrays.asList(ls);
            listInfoFiles.forEach(l->{
                try {
                    InfoFile infoFile = objectMapper.readValue(l,InfoFile.class);
                    infoFileArrayList.add(infoFile);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                InfoFile infoFile = objectMapper.readValue(String.valueOf(infoFiles),InfoFile.class);
                infoFileArrayList.add(infoFile);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        List<String> idsAttachment = new ArrayList<>();
        for (int i=0;i<multipartFiles.size();i++){
            try {
                String id = attachmentService.addAttachment(infoFileArrayList.get(i).getName(),
                        infoFileArrayList.get(i).getType(),
                        multipartFiles.get(i));
                idsAttachment.add(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok(idsAttachment);

    }

}
