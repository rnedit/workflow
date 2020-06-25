package kz.spring.workflow.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SetOrgUnitRequest {
    @NotNull
    String id;
    @NotNull
    String name;

    @NotNull
    private Boolean homeOrgUnit;
}
