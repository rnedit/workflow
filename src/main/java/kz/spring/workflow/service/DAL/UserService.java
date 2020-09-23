package kz.spring.workflow.service.DAL;

import kz.spring.workflow.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<User> findByUsername(String username);
    void save(User user);
    void delete(User user);
    Page<User> findAll(Pageable pageable);
    User getByUsername(String username);
    User getById(String id);
    User getByRefreshJwt(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findById(String id);
    Set<User> getUsersByParentIdProfileIsNull();
}
