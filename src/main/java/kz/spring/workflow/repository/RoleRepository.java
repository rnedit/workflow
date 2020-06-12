package kz.spring.workflow.repository;

import kz.spring.workflow.domain.ERole;
import kz.spring.workflow.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
