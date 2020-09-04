package kz.spring.workflow.request;

import lombok.Data;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import javax.validation.constraints.NotNull;

@Data
public class InternalRequest {

    @NotNull
    String userId;

    @NotNull
    Integer page;

    @NotNull
    Integer perPage;

}
