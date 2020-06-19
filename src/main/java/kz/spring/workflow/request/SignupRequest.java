package kz.spring.workflow.request;

import kz.spring.workflow.domain.Role;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class SignupRequest {
    @NotNull
    String Username;

    String Name;

    String Password;

    @NotNull
    String Email;

    @NotNull
    String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private Set<Role> roles = new HashSet<>();

}
