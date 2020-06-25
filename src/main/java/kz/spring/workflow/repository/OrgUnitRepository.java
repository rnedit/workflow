package kz.spring.workflow.repository;

import kz.spring.workflow.domain.OrgUnit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

public interface OrgUnitRepository extends MongoRepository<OrgUnit, Long> {
    Optional<OrgUnit> findByName(String name);
    OrgUnit getById(String id);

    Set<OrgUnit> getOrgUnitsByParentIdIsNullAndIdIsNot(String id);

    Set<OrgUnit> getOrgUnitsByParentIdNotNullOrHomeOrgUnit(Boolean b);
}
