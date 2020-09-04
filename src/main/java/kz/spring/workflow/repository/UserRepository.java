package kz.spring.workflow.repository;

import kz.spring.workflow.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User getByUsername(String username);
    User getById(String id);
    User getByRefreshJwt(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findById(String id);

    Set<User> getUsersByParentIdProfileIsNull();
}
