package kz.spring.workflow.domain.eventqueue;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Document(collection = "eventQueue")
public class EventQueue {

    @Id
    private String id;

    @NotNull
    private Date creationDate = new Date();

    @NotEmpty
    @NotNull
    private String documentId;

    @NotEmpty
    @NotNull
    private String taskName;

    @NotEmpty
    @NotNull
    private String creatorUserId;

    private Boolean executeSuccess;
}
