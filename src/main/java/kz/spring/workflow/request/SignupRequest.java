package kz.spring.workflow.request;

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

    private Set<String> roles = new HashSet<>();

}
