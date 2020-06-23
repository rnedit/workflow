package kz.spring.workflow.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrgUnitsRequest {

    @NotNull
    String name;

}
