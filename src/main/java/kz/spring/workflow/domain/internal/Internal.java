package kz.spring.workflow.domain.internal;


import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.domain.internal.types.InternalType;
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
    private Integer TypeAgreement = InternalType.NO_AGREEMENT;

    @NotNull
    private Boolean Draft = false;

    
    //AccessBlock
    @NotNull
    private String Recipient; //id

    @NotNull
    private List<String> AllReaders; //ids

    private List<String> AllReadersRoles; //ids

    //


    @DBRef
    private Profile сreatorProfile;

    @DBRef
    private User сreatorUser;

    @NotNull
    @DBRef
    private Profile profileRecipient;

    @NotNull
    @DBRef
    private Set<Profile> profilesAllReaders = new HashSet<>();

    @DBRef
    private Set<InternalAssignment> internalAssignments = new HashSet<>();

    @DBRef
    private Set<InternalPerformed> internalPerformeds = new HashSet<>();

    public static Internal setNewInternal(Internal internal) {
        Internal internalData = new Internal();
        internalData.setId(internal.getId());
        internalData.setCreationDate(internal.getCreationDate());
        internalData.setSubject(internal.getSubject());
        internalData.setNumber(internal.getNumber());
        internalData.setTypeAgreement(internal.getTypeAgreement());
        internalData.setDraft(internal.getDraft());
        internalData.setRecipient(internal.getRecipient());
        internalData.setAllReaders(internal.getAllReaders());
        internalData.setAllReadersRoles(internal.getAllReadersRoles());
        internalData.setProfileRecipient(internal.getProfileRecipient());
        internalData.setProfilesAllReaders(internal.getProfilesAllReaders());
        internalData.setInternalAssignments(internal.getInternalAssignments());
        internalData.setInternalPerformeds(internal.getInternalPerformeds());
        return internal;
    }

    public void setInternal(Internal internal) {
        this.setId(internal.getId());
        this.setCreationDate(internal.getCreationDate());
        this.setSubject(internal.getSubject());
        this.setNumber(internal.getNumber());
        this.setTypeAgreement(internal.getTypeAgreement());
        this.setDraft(internal.getDraft());
        this.setRecipient(internal.getRecipient());
        this.setAllReaders(internal.getAllReaders());
        this.setAllReadersRoles(internal.getAllReadersRoles());
        this.setProfileRecipient(internal.getProfileRecipient());
        this.setProfilesAllReaders(internal.getProfilesAllReaders());
        this.setInternalAssignments(internal.getInternalAssignments());
        this.setInternalPerformeds(internal.getInternalPerformeds());
    }
}
