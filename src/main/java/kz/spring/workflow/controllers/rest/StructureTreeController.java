package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.OrgUnit;
import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.repository.OrgUnitRepository;
import kz.spring.workflow.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;


@RestController
@PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR')")
@RequestMapping("/api/structuretree")
public class StructureTreeController {

    final
    private OrgUnitRepository orgUnitRepository;
    final
    private ProfileRepository profileRepository;
    @Autowired
    public StructureTreeController(OrgUnitRepository orgUnitRepository, ProfileRepository profileRepository) {
        this.orgUnitRepository = orgUnitRepository;
        this.profileRepository = profileRepository;
    }

    @PostMapping()
    public ResponseEntity<?> getAllStructureTree() {
        Set<OrgUnit> orgUnitSet = orgUnitRepository.getOrgUnitsByParentIdNotNullOrHomeOrgUnit(true);
        Set<Profile> profileSet = profileRepository.getProfilesByParentIdNotNull();

        Set<Object> objectCollection = new HashSet<>();
        if (orgUnitSet.size()>0) {
            orgUnitSet.forEach(orgUnit -> {
                objectCollection.add(orgUnit);
            });
        }
        if (profileSet.size()>0) {
            profileSet.forEach(p -> {
                objectCollection.add(p);
            });
        }

        return ResponseEntity.ok(objectCollection);
    }
}
