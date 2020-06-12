package kz.spring.workflow.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "profiles")
public class Profile {

    @Id
    private String id;

    @NotBlank
    @Size(max = 255)
    private String name;


    @NotBlank
    @Size(max = 255)
    private String parentId;

    @DBRef
    private Set<AccessProfile> access = new HashSet<>();

    public Profile(String name) {
        this.name = name;
    }
}
