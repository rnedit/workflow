package kz.spring.workflow.request;

import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class SetProfileRequest {
    @NotNull
    String id;
    @NotNull
    String name;
}
