package kz.spring.workflow.request.internal;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InternalSaveRequest {

    private String id;

    private Integer version;

    @NotEmpty
    @NotNull
    private String subject;

    @NotEmpty
    @NotNull
    private String recipient;

    @NotEmpty
    @NotNull
    private String recipientName;

    @NotNull
    private Integer typeAgreement;

    @NotNull
    private Boolean draft = false;

    @NotEmpty
    @NotNull
    private String creatorProfileId;

    @NotEmpty
    @NotNull
    private String creatorUserId;

    private String updateUserId;

    private String updateProfileId;

    private Boolean isAttachments;

    private String[] attachmentIds;

    private String[] attachmentNames;

    private Boolean isAnotherAttachments;

    private String[] anotherAttachmentIds;

    private String[] anotherAttachmentNames;

    private Object _persist;
}
