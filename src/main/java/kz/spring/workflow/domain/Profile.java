package kz.spring.workflow.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
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
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String parentId;

    @NotNull
    private Date creationDate;

    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "orgunits_id", nullable = false)
    private OrgUnit orgUnit;

    @DBRef
    private Set<AccessProfile> access = new HashSet<>();

    public Profile(String name, String parentId, Date creationDate) {
        this.name = name;
        this.parentId = parentId;
        this.creationDate = creationDate;
    }
}
