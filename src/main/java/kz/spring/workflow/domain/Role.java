package kz.spring.workflow.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "roles")
public class Role {

    @Id
    private String id;

    private ERole name;

    public Role(String id, ERole name) {
        this.id = id;
        this.name = name;
    }

}
