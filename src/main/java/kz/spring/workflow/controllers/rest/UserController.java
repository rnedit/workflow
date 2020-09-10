package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.controllers.rest.utils.ApiUtils;
import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.repository.ProfileRepository;
import kz.spring.workflow.repository.UserRepository;
import kz.spring.workflow.request.SignupRequest;
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
@RequestMapping("/api/users")
public class UserController {

    final
    private UserRepository userRepository;

    final
    private ProfileRepository profileRepository;

    final
    private ApiUtils apiUtils;

    public UserController(UserRepository userRepository, ProfileRepository profileRepository, ApiUtils apiUtils) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.apiUtils = apiUtils;
    }

    @PostMapping("/parentidisnull")
    public ResponseEntity<?> getUsersByParentIdIsNull() {
        Set<User> users = userRepository.getUsersByParentIdProfileIsNull();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/getaccessprofile/{id}")
    public ResponseEntity<?> getAccessProfile(@PathVariable String id) {
        User user = userRepository.getById(id);
        Map<String, String> error = new HashMap<>();

        if (user != null) {
            if (user.getParentIdProfile() == null) {
                Map<String, Object> data = new HashMap<>();
                data.put("SUCCESS", "user.getParentId() null");
                data.put("accessprofile", null);
                return ResponseEntity.ok(data);
            }
            Profile profile = profileRepository.getById(user.getParentIdProfile());
            if (profile != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("accessprofile", profile.getAccess());
                return ResponseEntity.ok(data);
            }
        }
        error.put("ERROR", "user null");
        error.put("code", "2");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @PostMapping()
    public ResponseEntity<?> users(@Valid @RequestBody UsersRequest usersRequest,
                                   BindingResult bindingResult) {
        Map<String, String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR", "users null");
            error.put("code", "2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        Pageable sortedByName = PageRequest.of(usersRequest.getPage() - 1, usersRequest.getPerpage());

        Map<String, Object> data = new HashMap<>();
        Page<User> users = userRepository.findAll(sortedByName);

        Collection<User> collection = users.getContent();
        data.put("users", collection);
        data.put("perpage", usersRequest.getPerpage());
        data.put("page", usersRequest.getPage());
        data.put("total", userRepository.count());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/edit/{id}")
    ResponseEntity<?> editUser(@PathVariable String id, @Valid @RequestBody SignupRequest signUpRequest,
                               BindingResult bindingResult) {
        Map<String, String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR", "editUser null");
            error.put("code", "2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        User user = userRepository.getById(id);
        User userUpdate = userRepository.getByUsername(signUpRequest.getUsername());

        if (!user.getId().equals(userUpdate.getId()))
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                error.put("ERROR", "User Name Exist");
                error.put("code", "0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
        if (!user.getId().equals(userUpdate.getId()))
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                error.put("ERROR", "Email Exist");
                error.put("code", "1");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());

        user.setRoles(apiUtils.calcRoles(signUpRequest.getRoles()));
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable String id) {
        User user = userRepository.getById(id);
        userRepository.delete(user);
        Map<String, String> inf = new HashMap<>();
        inf.put("SUCCESS", "User Deleted");
        inf.put("code", "0");
        return ResponseEntity.ok(inf);
    }

}
