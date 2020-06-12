package kz.spring.workflow.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "access")
public class AccessProfile {

    @Id
    private String id;

    private EAccessProfile name;

    public AccessProfile(EAccessProfile name) {
        this.name = name;
    }
}
