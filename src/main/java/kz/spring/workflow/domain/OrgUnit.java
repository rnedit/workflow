package kz.spring.workflow.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;


@Data
@Document(collection = "orgunits")
public class OrgUnit {

    @Id
    private String id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Date creationDate = new Date();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orgUnit")
    private Set<Profile> profiles;

    public OrgUnit(String name) {
        this.name = name;
    }
}
