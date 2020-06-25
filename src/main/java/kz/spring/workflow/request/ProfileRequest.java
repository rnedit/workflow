package kz.spring.workflow.request;


import kz.spring.workflow.domain.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProfileRequest {

    @NotNull
    String name;

    String parentId; //орг. единица

    String parentName; //орг. единица

    @NotNull
    User user;

   // @NotNull
   // String userId;

    String oldUserId;

    @NotNull
    private Set<String> access= new HashSet<>();

}
