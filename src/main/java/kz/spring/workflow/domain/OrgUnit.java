package kz.spring.workflow.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@Document(collection = "orgunits")
public class OrgUnit {

    @Id
    private String id;

    @NotNull
    private String type = "orgunit";

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String parentId; //<<- OrgUnit ID

    @Size(max = 255)
    private String parentName; //<<- OrgUnit ID

    @NotNull
    private Boolean homeOrgUnit = false;

    @NotNull
    private String suffix;

    @NotNull
    private Date creationDate = new Date();

    @DBRef
    private Set<Profile> profiles = new HashSet<>();

    @DBRef
    private Set<OrgUnit> OrgUnits = new HashSet<>();

    public OrgUnit(String name) {
        this.name = name;
    }

}
