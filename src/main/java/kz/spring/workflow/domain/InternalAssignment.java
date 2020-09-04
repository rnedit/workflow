package kz.spring.workflow.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "internalAssignment")
public class InternalAssignment {
    @Id
    private String id;

    @NotNull
    private Date CreationDate = new Date();

    @NotNull
    private String Number;

    @NotNull
    private String RecipientProfileId;

    @NotNull
    private String RecipientProfileName;

    private List<String> AllReaders;

    private List<String> AllReadersRoles;

    @DBRef
    private Internal internal;

    @DBRef
    private Set<InternalPerformed> internalPerformeds = new HashSet<>();
}
