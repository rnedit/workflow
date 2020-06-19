package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.AccessProfile;
import kz.spring.workflow.domain.EAccessProfile;
import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;
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
@RequestMapping("/api/orgunits")
public class OrgUnitController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    AccessRepository accessRepository;

    @PostMapping()
    public ResponseEntity<?> orgunits(@Valid @RequestBody UsersRequest usersRequest,
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
        data.put("orgunits", collection );
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
        User user = userRepository.getByUsername(profileRequest.getUserId());

        User userProf = user.createBlankUser();
        userProf.setId(user.getId());
        userProf.setUsername(user.getUsername());
        userProf.setEmail(user.getEmail());
        userProf.setFirstName(user.getFirstName());
        userProf.setLastName(user.getLastName());
        userProf.setRoles(user.getRoles());

        profile.setUser(userProf);
        
        profileRepository.save(profile);
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

        User user = userRepository.getByUsername(profileRequest.getUserId());

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

}
