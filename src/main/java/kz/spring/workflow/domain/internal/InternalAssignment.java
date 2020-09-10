package kz.spring.workflow.domain.internal;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.internal.attribute.AbstractInternal;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "internalAssignment")
public class InternalAssignment extends AbstractInternal {

    //AccessBlock
    @NotNull
    private List<String> performers;
    //

    @NotNull
    @DBRef
    private Set<Profile> profilesPerformers = new HashSet<>();

    @NotNull
    @DBRef
    private Internal internal;

    @DBRef
    private Set<InternalPerformed> internalPerformeds = new HashSet<>();
}
