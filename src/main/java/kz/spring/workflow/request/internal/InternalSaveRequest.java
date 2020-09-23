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

    @NotNull
    private Integer typeAgreement;

    @NotNull
    private Boolean draft = false;

    @NotEmpty
    @NotNull
    private String сreatorProfileId;

    @NotEmpty
    @NotNull
    private String сreatorUserId;

    private String updateUserId;

    private String updateProfileId;

    @NotEmpty
    @NotNull
    private List<String> сreatorRolesId;


    private String[] attachmentIds;

    private String[] attachmentNames;
}
