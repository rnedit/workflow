package kz.spring.workflow.domain.configuration.attribute;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public abstract class AbstractConfigure {

    @Id
    private String id;

    @NotNull
    private Date creationDate = new Date();

    @NotEmpty
    @NotNull
    private String name;

}
