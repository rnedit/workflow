package kz.spring.workflow.request.internal;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InternalSaveRequest {

    @NotEmpty
    @NotNull
    private String Subject;

    @NotEmpty
    @NotNull
    private String Recipient;

    @NotNull
    private Integer TypeAgreement;

    @NotNull
    private Boolean Draft = false;

    @NotEmpty
    @NotNull
    private String сreatorProfileId;

    @NotEmpty
    @NotNull
    private List<String> сreatorRolesId;

    @NotEmpty
    @NotNull
    private String сreatorUserId;

}
