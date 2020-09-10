package kz.spring.workflow.domain.configuration;

import kz.spring.workflow.domain.configuration.attribute.AbstractConfigure;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "numerator")
public class Numerator extends AbstractConfigure {

    @NotEmpty
    @NotNull
    private String key = "";

    private String prefix = "";
    private String postfix = "";

    @NotNull
    private Long number = 0L;

    public static Numerator setNewNumerator(Numerator numerator) {
        Numerator nData = new Numerator();
            nData.setNumber(numerator.getNumber());
            nData.setPostfix(numerator.getPostfix());
            nData.setPrefix(numerator.getPrefix());
            nData.setKey(numerator.getKey());
            nData.setId(numerator.getId());
            nData.setCreationDate(numerator.getCreationDate());
            nData.setName(numerator.getName());
        return nData;
    }
}
