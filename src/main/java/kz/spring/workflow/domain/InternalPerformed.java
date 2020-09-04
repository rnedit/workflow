package kz.spring.workflow.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "internalPerformed")
public class InternalPerformed {
    @Id
    private String id;

    @NotNull
    private Date CreationDate = new Date();

    @NotNull
    private String Number;

    private List<String> AllReaders;

    private List<String> AllReadersRoles;

    @DBRef
    private InternalAssignment internalAssignment;
}
