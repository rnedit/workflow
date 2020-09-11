package kz.spring.workflow.request.files;

import lombok.Data;

@Data
public class InfoFile {

   private Float lastModified;
   private String name;
   private Float size;
   private String type;
}
