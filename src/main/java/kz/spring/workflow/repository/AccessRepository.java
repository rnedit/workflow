package kz.spring.workflow.repository;

import kz.spring.workflow.domain.AccessProfile;
import kz.spring.workflow.domain.EAccessProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccessRepository extends MongoRepository<AccessProfile, String> {
    Optional<AccessProfile> findByName(EAccessProfile name);
}
