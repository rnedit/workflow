package kz.spring.workflow.domain;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String firstName;

    @NotBlank
    @Size(max = 255)
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 255)
    private String refreshJwt;

    @NotNull
    private Integer refreshJwtMaxAge;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public User(String username, String name, String email, String password, String firstName,
                String lastName, Integer refreshJwtMaxAge) {
        this.username = username;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.refreshJwtMaxAge = refreshJwtMaxAge;
    }

}
