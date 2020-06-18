package kz.spring.workflow.repository;

import kz.spring.workflow.domain.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProfileRepository extends MongoRepository<Profile, Long> {
    Optional<Profile> findByName(String name);
    Profile getById(String id);
}
