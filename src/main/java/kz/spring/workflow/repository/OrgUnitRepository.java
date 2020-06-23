package kz.spring.workflow.repository;

import kz.spring.workflow.domain.OrgUnit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrgUnitRepository extends MongoRepository<OrgUnit, Long> {
    Optional<OrgUnit> findByName(String name);
    OrgUnit getById(String id);
}
