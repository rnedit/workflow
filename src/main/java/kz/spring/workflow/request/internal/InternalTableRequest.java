package kz.spring.workflow.request.internal;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class InternalTableRequest {

    @NotEmpty
    @NotNull
    String userId;

    @NotNull
    Integer page = 1;

    @NotNull
    Integer perPage = 15;

    @NotNull
    Integer countExec;


}
