package kz.spring.workflow.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SignupRequest {
    String Username;
    String Name;
    String Password;
    String Email;
    String firstName;
    private String lastName;

    private Set<String> roles = new HashSet<>();

}
