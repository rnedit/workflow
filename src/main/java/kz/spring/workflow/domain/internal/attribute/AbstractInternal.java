package kz.spring.workflow.domain.internal.attribute;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.domain.internal.Internal;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public abstract class AbstractInternal {
    @Id
    private String id;

    @NotEmpty
    @NotNull
    private String number;

    @NotNull
    private Date creationDate = new Date();

    @NotNull
    private List<String> allReaders; //ids

    private List<String> allReadersRoles; //ids

    @DBRef
    private Profile сreatorProfile;

    @DBRef
    private User сreatorUser;

    @NotNull
    @DBRef
    private Set<Profile> profilesAllReaders = new HashSet<>();

}
