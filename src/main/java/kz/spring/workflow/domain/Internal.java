package kz.spring.workflow.domain;


import kz.spring.workflow.domain.Types.InternalType;
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
@Document(collection = "internal")
public class Internal {

    @Id
    private String id;

    @NotNull
    private Date CreationDate = new Date();

    @NotNull
    private String Subject;

    @NotNull
    private String Number;

    @NotNull
    private String RecipientProfileId;

    @NotNull
    private String RecipientProfileName;

    @NotNull
    private Integer TypeAgreement = InternalType.NO_AGREEMENT;

    @NotNull
    private Boolean Draft = false;

    private List<String> AllReaders;

    private List<String> AllReadersRoles;

    @DBRef
    private Set<InternalAssignment> internalAssignments = new HashSet<>();

    @DBRef
    private Set<InternalPerformed> internalPerformeds = new HashSet<>();
}
