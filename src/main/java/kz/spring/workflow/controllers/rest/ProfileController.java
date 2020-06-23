package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.*;
import kz.spring.workflow.repository.AccessRepository;
import kz.spring.workflow.repository.ProfileRepository;
import kz.spring.workflow.repository.UserRepository;
import kz.spring.workflow.request.ProfileRequest;
import kz.spring.workflow.request.UsersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR')")
@RequestMapping("/api/profiles")
public class ProfileController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    AccessRepository accessRepository;

    @PostMapping()
    public ResponseEntity<?> profiles(@Valid @RequestBody UsersRequest usersRequest,
                                      BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","UsersRequest null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Pageable sortedByName = PageRequest.of(usersRequest.getPage()-1, usersRequest.getPerpage());
        Map<String,Object> data = new HashMap<>();
        Page<Profile> profiles = profileRepository.findAll(sortedByName);
        Collection<Profile> collection = profiles.getContent();
        data.put("profiles", collection );
        data.put("perpage",usersRequest.getPerpage());
        data.put("page",usersRequest.getPage());
        data.put("total",profileRepository.count());
        return ResponseEntity.ok(data);
    }
    @PostMapping("/add")
    ResponseEntity<?> addProfile(@Valid @RequestBody ProfileRequest profileRequest,
                                 BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","ProfileRequest null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Profile profile = new Profile(
                profileRequest.getName(),
                profileRequest.getParentId(),
                new Date()
        );
        User user = userRepository.getByUsername(profileRequest.getUser().getUsername());

        User userProf = user.createBlankUser();
        userProf.setId(user.getId());
        userProf.setUsername(user.getUsername());
        userProf.setEmail(user.getEmail());
        userProf.setFirstName(user.getFirstName());
        userProf.setLastName(user.getLastName());
        userProf.setRoles(user.getRoles());

        profile.setUser(userProf);

        Set<String> strAccess = profileRequest.getAccess();
        Set<AccessProfile> access = calcAccess(strAccess);
        profile.setAccess(access);
        profileRepository.save(profile);
        user.setParentId(profile.getId());
        userRepository.save(user);
        return ResponseEntity.ok(profile);
    }

    @PostMapping("/edit/{id}")
    ResponseEntity<?> editProfile(@PathVariable String id, @Valid @RequestBody ProfileRequest profileRequest,
                                  BindingResult bindingResult) {

        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","ProfileRequest null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Profile profile = profileRepository.getById(id);
        profile.setName(profileRequest.getName());
        profile.setParentId(profileRequest.getParentId());

        User user = userRepository.getByUsername(profileRequest.getUser().getUsername());

        User oldUser = userRepository.getById(profileRequest.getOldUserId());

        User userProf = user.createBlankUser();
        userProf.setId(user.getId());
        userProf.setUsername(user.getUsername());
        userProf.setEmail(user.getEmail());
        userProf.setFirstName(user.getFirstName());
        userProf.setLastName(user.getLastName());
        userProf.setRoles(user.getRoles());

        profile.setUser(userProf);

        Set<String> strAccess = profileRequest.getAccess();
        Set<AccessProfile> access = calcAccess(strAccess);
        profile.setAccess(access);

            profileRepository.save(profile);

            oldUser.setParentId(null);
            userRepository.save(oldUser);

            user.setParentId(profile.getId());
            userRepository.save(user);

        return ResponseEntity.ok(profile);
    }

    @PostMapping("/delete/{id}")
    ResponseEntity<?> deleteProfile(@PathVariable String id) {
        Profile profile = profileRepository.getById(id);

        User userProfile = profile.getUser();
        if (userProfile!=null) {
            User user = userRepository.getById(userProfile.getId());
            if (user!=null) {
                user.setParentId(null);
                userRepository.save(user);
            }
        }
        profileRepository.delete(profile);
        Map<String,String> inf = new HashMap<>();
        inf.put("SUCCESS","Profile Deleted");
        inf.put("code","0");
        return ResponseEntity.ok(inf);
    }

    private Set<AccessProfile> calcAccess(Set<String> strAccess) {
        Set<AccessProfile> access = new HashSet<>();
        AccessProfile profileAccess;
        //default Access
        if (strAccess == null) {
            profileAccess = accessRepository.findByName(EAccessProfile.ACCESS_CREATEDOCUMENT)
                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
            access.add(profileAccess);
            profileAccess = accessRepository.findByName(EAccessProfile.ACCESS_SZ)
                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
            access.add(profileAccess);
            profileAccess = accessRepository.findByName(EAccessProfile.ACCESS_STRUCT)
                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
            access.add(profileAccess);
        } else {
            if (!strAccess.isEmpty()) {
                strAccess.forEach(acc ->{
                    switch (acc) {
                        case "ACCESS_CREATEDOCUMENT":
                            AccessProfile profileAccess0 = accessRepository.findByName(EAccessProfile.ACCESS_CREATEDOCUMENT)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess0);

                            break;
                        case "ACCESS_CREATEPROFILE":
                            AccessProfile profileAccess1 = accessRepository.findByName(EAccessProfile.ACCESS_CREATEPROFILE)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess1);
                            break;
                        case "ACCESS_CREATEUSER":
                            AccessProfile profileAccess2 = accessRepository.findByName(EAccessProfile.ACCESS_CREATEUSER)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess2);
                            break;
                        case "ACCESS_DELETEPROFILE":
                            AccessProfile profileAccess3 = accessRepository.findByName(EAccessProfile.ACCESS_DELETEPROFILE)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess3);
                            break;
                        case "ACCESS_DELETEUSER":
                            AccessProfile profileAccess4 = accessRepository.findByName(EAccessProfile.ACCESS_DELETEUSER)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess4);
                            break;
                        case "ACCESS_INDOC":
                            AccessProfile profileAccess5 = accessRepository.findByName(EAccessProfile.ACCESS_INDOC)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess5);
                            break;
                        case "ACCESS_ORD":
                            AccessProfile profileAccess6 = accessRepository.findByName(EAccessProfile.ACCESS_ORD)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess6);
                            break;
                        case "ACCESS_OUTDOC":
                            AccessProfile profileAccess7 = accessRepository.findByName(EAccessProfile.ACCESS_OUTDOC)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess7);
                            break;
                        case "ACCESS_SPRAV":
                            AccessProfile profileAccess8 = accessRepository.findByName(EAccessProfile.ACCESS_SPRAV)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess8);
                            break;
                        case "ACCESS_STRUCT":
                            AccessProfile profileAccess9 = accessRepository.findByName(EAccessProfile.ACCESS_STRUCT)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess9);
                            break;
                        case "ACCESS_SZ":
                            AccessProfile profileAccess10 = accessRepository.findByName(EAccessProfile.ACCESS_SZ)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess10);
                            break;

                        case "ACCESS_ALL":
                            AccessProfile profileAccess11 = accessRepository.findByName(EAccessProfile.ACCESS_ALL)
                                    .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                            access.add(profileAccess11);
                            break;
                    }
                });
            } else {
                profileAccess = accessRepository.findByName(EAccessProfile.ACCESS_CREATEDOCUMENT)
                        .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                access.add(profileAccess);
                profileAccess = accessRepository.findByName(EAccessProfile.ACCESS_SZ)
                        .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                access.add(profileAccess);
                profileAccess = accessRepository.findByName(EAccessProfile.ACCESS_STRUCT)
                        .orElseThrow(() -> new RuntimeException("Error: Access is not found."));
                access.add(profileAccess);
            }
        }

        return access;
    }

}
