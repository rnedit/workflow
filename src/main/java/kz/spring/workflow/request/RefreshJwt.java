package kz.spring.workflow.request;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RefreshJwt {
    @NotNull
    String refreshJwt;
}
