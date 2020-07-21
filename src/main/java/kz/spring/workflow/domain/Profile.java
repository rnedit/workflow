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
@Document(collection = "profiles")
public class Profile {

    @Id
    private String id;

    @NotNull
    private String type = "profile";

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String parentId; //<<- OrgUnit ID

    @Size(max = 255)
    private String parentName; //<<- OrgUnit Name

    @NotNull
    private Date creationDate = new Date();

    @NotNull
    private User user;

    @DBRef
    private Set<AccessProfile> access = new HashSet<>();

    public Profile(String name) {
        this.name = name;

    }
}
