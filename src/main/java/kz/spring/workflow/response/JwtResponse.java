package kz.spring.workflow.response;

import kz.spring.workflow.domain.ERole;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JwtResponse {
    String id;
    String username;
    String firstName;
    String name;
    Boolean loggedIn;
    String lastName;
    String refreshJwt;
    Integer refreshJwtMaxAge;

    Date updatedJwt;
    List<ERole> roles;

    public JwtResponse(  String id,
                        String username,
                        String firstName,
                        String lastName,
                         String name,
                        Boolean loggedIn,
                        String refreshJwt,
                        Integer refreshJwtMaxAge,
                        Date updatedJwt,
                        List<ERole> roles
    ) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.loggedIn = loggedIn;
        this.refreshJwt = refreshJwt;
        this.refreshJwtMaxAge = refreshJwtMaxAge;
        this.updatedJwt = updatedJwt;
        this.roles = roles;
    }

}
