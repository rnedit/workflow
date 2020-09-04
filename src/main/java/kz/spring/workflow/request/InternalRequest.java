package kz.spring.workflow.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class InternalRequest {

    @NotEmpty
    @NotNull
    private String id;
}