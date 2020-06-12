package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.User;
import kz.spring.workflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR')")
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping()
    public Collection<User> users() {
        return userRepository.findAll();
    }


    @PostMapping("/{id}")
    ResponseEntity<?> getUser(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
