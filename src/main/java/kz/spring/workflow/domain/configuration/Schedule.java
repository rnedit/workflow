package kz.spring.workflow.domain.configuration;

import kz.spring.workflow.domain.configuration.attribute.AbstractConfigure;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "configuration")
public class Schedule extends AbstractConfigure {

    @NotEmpty
    @NotNull
    private String form = "Schedule";

}
