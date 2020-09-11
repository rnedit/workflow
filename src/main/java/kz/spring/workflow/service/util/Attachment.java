package kz.spring.workflow.service.util;

import lombok.Data;

import java.io.InputStream;

@Data
public class Attachment {
    private String name;
    private String type;
    private InputStream stream;
}

