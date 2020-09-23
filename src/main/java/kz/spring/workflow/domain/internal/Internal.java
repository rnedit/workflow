package kz.spring.workflow.domain.internal;


import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.internal.attribute.AbstractInternal;
import kz.spring.workflow.domain.internal.types.InternalType;
import kz.spring.workflow.repository.ProfileRepository;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "internal")
public class Internal extends AbstractInternal {

    @NotNull
    private String subject;

    @NotNull
    private Integer typeAgreement = InternalType.NO_AGREEMENT;

    @NotNull
    private Boolean draft = false;

    //AccessBlock
    @NotNull
    private String recipient; //id

    //

    @NotNull
    @DBRef
    private Profile profileRecipient;

    @DBRef
    private Set<InternalAssignment> internalAssignments = new HashSet<>();

    @DBRef
    private Set<InternalPerformed> internalPerformeds = new HashSet<>();

    private List<String> attachments;

    private List<String> attachmentNames;

    public static Internal setNewInternalFromCurrent(Internal internal) {
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
        internalData.setAttachments(internal.getAttachments());
        internalData.setAttachmentNames(internal.getAttachmentNames());
        return internal;
    }

}
