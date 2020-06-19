package kz.spring.workflow.request;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProfileRequest {

    @NotNull
    String name;

    String parentId; //орг. единица

    @NotNull
    String userId;

    String oldUserId;

    @NotNull
    private Set<String> access= new HashSet<>();

}
