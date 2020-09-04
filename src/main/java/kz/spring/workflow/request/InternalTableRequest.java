package kz.spring.workflow.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class InternalTableRequest {


    @NotEmpty
    @NotNull
    String userId;

    @NotNull
    Integer page = 1;

    @NotNull
    Integer perPage = 15;

}
