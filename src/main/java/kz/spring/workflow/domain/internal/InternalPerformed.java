package kz.spring.workflow.domain.internal;

import kz.spring.workflow.domain.internal.attribute.AbstractInternal;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


@Data
@Document(collection = "internalPerformed")
public class InternalPerformed extends AbstractInternal {

    @NotNull
    @DBRef
    private InternalAssignment internalAssignment;
}
