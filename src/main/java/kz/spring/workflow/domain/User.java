package kz.spring.workflow.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotNull
    @Size(max = 20)
    private String username;

    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Size(max = 50)
    @Email
    private String email;

    @NotNull
    @Size(max = 120)
    private String password;

    @Size(max = 255)
    private String refreshJwt;

    @NotNull
    private Date creationDate;

    private String parentId; //профайл

    @NotNull
    private Integer refreshJwtMaxAge;

    @DBRef
    private Set<Role> roles = new HashSet<>();

    public User(String username, String name, String email, String password, String firstName,
                String lastName, Integer refreshJwtMaxAge, Date creationDate) {
        this.username = username;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.refreshJwtMaxAge = refreshJwtMaxAge;
        this.creationDate = creationDate;
    }

    private User () {}

    public User createBlankUser() {
       return new User();
    }

}
