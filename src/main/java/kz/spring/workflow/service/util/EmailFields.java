package kz.spring.workflow.service.util;

import lombok.Data;

@Data
public class EmailFields {

    private String[] to;
    private String subject;
    private String body;
    private String pathToAttachment;

}
