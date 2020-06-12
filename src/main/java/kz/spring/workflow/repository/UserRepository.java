package kz.spring.workflow.repository;

import kz.spring.workflow.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    User getByUsername(String username);
    User getByRefreshJwt(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<User> findById(String id);
}
