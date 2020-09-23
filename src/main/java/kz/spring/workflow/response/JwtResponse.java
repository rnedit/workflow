package kz.spring.workflow.response;

import kz.spring.workflow.domain.ERole;
import kz.spring.workflow.domain.Profile;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class JwtResponse {
    String id;
    String username;
    String firstName;
    String name;
    Boolean loggedIn;
    String lastName;
    String refreshJwt;
    String jwtID;
    Integer refreshJwtMaxAge;
    Boolean editable;

    Date updatedJwt;

    List<ERole> roles;

    Set<String> rolesId;

    String creationDate;

    Profile profile;

    public JwtResponse(  String id,
                        String username,
                        String firstName,
                        String lastName,
                        String name,
                        Boolean loggedIn,
                        String refreshJwt,
                         String jwtID,
                        Integer refreshJwtMaxAge,
                        Date updatedJwt,
                        List<ERole> roles,
                         Set<String> rolesId,
                        String creationDate,
                        Boolean editable,
                         Profile profile
    ) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.loggedIn = loggedIn;
        this.refreshJwt = refreshJwt;
        this.jwtID = jwtID;
        this.refreshJwtMaxAge = refreshJwtMaxAge;
        this.updatedJwt = updatedJwt;
        this.roles = roles;
        this.rolesId = rolesId;
        this.creationDate = creationDate;
        this.editable = editable;
        this.profile = profile;
    }

}
