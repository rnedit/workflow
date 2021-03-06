package kz.spring.workflow.domain.internal.attribute;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;

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

    private Integer version = 1;

    @Id
    private String id;

    private String number;

    @NotNull
    private Date creationDate = new Date();

    @NotNull
    private List<String> allReaders; //ids

    private List<String> allReadersRoles; //ids

    @NotEmpty
    @NotNull
    private String creatorProfileId;

    @DBRef
    private Profile creatorProfile;

    @NotEmpty
    @NotNull
    private String creatorUserId;

    @DBRef
    private User creatorUser;

    @DBRef
    private Profile updateProfile;

    private String updateProfileId;

    @DBRef
    private Profile updateUser;

    private String updateUserId;


    @NotNull
    @DBRef
    private Set<Profile> profilesAllReaders = new HashSet<>();

}
