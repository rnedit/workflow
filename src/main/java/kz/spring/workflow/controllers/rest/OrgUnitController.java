package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.OrgUnit;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.repository.OrgUnitRepository;
import kz.spring.workflow.repository.ProfileRepository;
import kz.spring.workflow.request.OrgUnitsRequest;
import kz.spring.workflow.request.SetOrgUnitRequest;
import kz.spring.workflow.request.SetProfileRequest;
import kz.spring.workflow.request.UsersRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@PreAuthorize("hasAnyRole('USER','ADMIN','MODERATOR')")
@RequestMapping("/api/orgunits")
public class OrgUnitController {

    final
    private OrgUnitRepository orgUnitRepository;
    final
    private ProfileRepository profileRepository;

    public OrgUnitController(OrgUnitRepository orgUnitRepository, ProfileRepository profileRepository) {
        this.orgUnitRepository = orgUnitRepository;
        this.profileRepository = profileRepository;
    }

    @PostMapping()
    public ResponseEntity<?> orgUnits(@Valid @RequestBody UsersRequest usersRequest,
                                      BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","UsersRequest null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        Pageable sortedByName = PageRequest.of(usersRequest.getPage()-1, usersRequest.getPerpage());
        Map<String,Object> data = new HashMap<>();
        Page<OrgUnit> orgUnits = orgUnitRepository.findAll(sortedByName);
        Collection<OrgUnit> collection = orgUnits.getContent();
        data.put("orgunits", collection );
        data.put("perpage",usersRequest.getPerpage());
        data.put("page",usersRequest.getPage());
        data.put("total",orgUnitRepository.count());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/add")
    ResponseEntity<?> addOrgUnit(@Valid @RequestBody OrgUnitsRequest orgUnitsRequest,
                                 BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","OrgUnitsRequest null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        OrgUnit orgUnit = new OrgUnit(orgUnitsRequest.getName());

        orgUnitRepository.save(orgUnit);
        return ResponseEntity.ok(orgUnit);
    }

    @PostMapping("/edit/{id}")
    ResponseEntity<?> editOrgUnit(@PathVariable String id, @Valid @RequestBody OrgUnitsRequest orgUnitsRequest,
                                  BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","OrgUnitsRequest null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        OrgUnit orgUnit = orgUnitRepository.getById(id);
        orgUnit.setName(orgUnitsRequest.getName());
        orgUnitRepository.save(orgUnit);

        return ResponseEntity.ok(orgUnit);
    }

    @PostMapping("/delete/{id}")
    ResponseEntity<?> deleteOrgUnit(@PathVariable String id) {
        OrgUnit orgUnit = orgUnitRepository.getById(id);
        orgUnitRepository.delete(orgUnit);

        Map<String,String> inf = new HashMap<>();
        inf.put("SUCCESS","OrgUnit Deleted");
        inf.put("code","0");
        return ResponseEntity.ok(inf);
    }

    @PostMapping("/getprofiles/{id}")
    ResponseEntity<?> getProfiles(@PathVariable String id) {
        OrgUnit orgUnit = orgUnitRepository.getById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("profiles",orgUnit.getProfiles());
        return ResponseEntity.ok(data);
    }

    @PostMapping("/getorgunits/{id}")
    ResponseEntity<?> getOrgUnits(@PathVariable String id) {
        OrgUnit orgUnit = orgUnitRepository.getById(id);
        Map<String,Object> data = new HashMap<>();
        data.put("orgunits",orgUnit.getOrgUnits());
        return ResponseEntity.ok(data);
    }
    @PostMapping("/sethomeorgunit")
    ResponseEntity<?> setHomeOrgUnit(@Valid @RequestBody SetOrgUnitRequest setOrgUnitsRequest,
                                     BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","setHomeOrgUnit null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        OrgUnit orgUnit = orgUnitRepository.getById(setOrgUnitsRequest.getId());
        orgUnit.setHomeOrgUnit(setOrgUnitsRequest.getHomeOrgUnit());
        orgUnitRepository.save(orgUnit);
        return ResponseEntity.ok(orgUnit);
    }

    @PostMapping("/setprofiles/{id}")
    ResponseEntity<?> setProfiles(@PathVariable String id,
                                  @Valid @RequestBody List<SetProfileRequest> listProfilesRequest,
                                  BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","setProfiles null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        OrgUnit orgUnit = orgUnitRepository.getById(id);

        Set<Profile> oldProfiles = orgUnit.getProfiles();
        if (oldProfiles!=null) {
            oldProfiles.forEach(profile -> {
                profile.setParentId(null);
                profile.setParentName(null);
            });
            profileRepository.saveAll(oldProfiles);
            orgUnit.setProfiles(null);
        }

        Set<Profile> profileSet = new HashSet<>();
        listProfilesRequest.forEach(reqProf->{
            profileSet.add(profileRepository.getById(reqProf.getId()));
        });
        Set<OrgUnit> orgUnits = new HashSet<>();
        orgUnits.add(orgUnit);
        if (profileSet.size()>0) {
            orgUnit.setProfiles(profileSet);
            profileSet.forEach(profile -> {
                profile.setParentId(orgUnit.getId());
                profile.setParentName(orgUnit.getName());
            });
            profileRepository.saveAll(profileSet);
        }
        orgUnitRepository.save(orgUnit);

        return ResponseEntity.ok(orgUnit.getProfiles());
    }

    @PostMapping("/setorgunits/{id}")
    ResponseEntity<?> setOrgUnits(@PathVariable String id,
                                  @Valid @RequestBody List<SetOrgUnitRequest> setOrgUnitRequestList,
                                  BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","setOrgUnits null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        OrgUnit orgUnit = orgUnitRepository.getById(id);

        Set<OrgUnit> oldOrgUnits = orgUnit.getOrgUnits();
        if (oldOrgUnits!=null) {
            oldOrgUnits.forEach(o -> {
                o.setParentId(null);
                o.setParentName(null);
            });
            orgUnitRepository.saveAll(oldOrgUnits);
            orgUnit.setOrgUnits(null);
        }

        Set<OrgUnit> orgUnitHashSet = new HashSet<>();
        setOrgUnitRequestList.forEach(r->{
            orgUnitHashSet.add(orgUnitRepository.getById(r.getId()));
        });
        if (orgUnitHashSet.size()>0) {
            orgUnit.setOrgUnits(orgUnitHashSet);
            orgUnitHashSet.forEach(o -> {
                o.setParentId(orgUnit.getId());
                o.setParentName(orgUnit.getName());
            });
            orgUnitRepository.saveAll(orgUnitHashSet);
        }
        orgUnitRepository.save(orgUnit);

        return ResponseEntity.ok(orgUnit.getOrgUnits());
    }
    ///api/orgunits
    @PostMapping("/getorgunitsbyparentidisnullandidisnot/{id}")
    ResponseEntity<?> getOrgUnitsByParentIdIsNullAndIdIsNot(@PathVariable String id) {
        OrgUnit orgUnit = orgUnitRepository.getById(id);
        Set<OrgUnit> orgUnits= orgUnit.getOrgUnits();
        Set<OrgUnit> ParentIdIsNullAndIdIsNot = orgUnitRepository.getOrgUnitsByParentIdIsNullAndIdIsNot(id);
        Map<String,Object> data = new HashMap<>();
        data.put("orgUnits",orgUnits);
        data.put("ParentIdIsNullAndIdIsNot",ParentIdIsNullAndIdIsNot);
        return ResponseEntity.ok(data);
    }
///api/orgunits
    @PostMapping("/getprofilesandprofilesparentidisnull/{id}")
    ResponseEntity<?> getProfilesAndProfilesParentIdIsNull(@PathVariable String id) {
        OrgUnit orgUnit = orgUnitRepository.getById(id);
        Set<OrgUnit> orgUnitsForParent = orgUnitRepository.getOrgUnitsByParentIdIsNullAndIdIsNot(id);
        Set<Profile> profiles = orgUnit.getProfiles();
        Set<Profile> profilesParentIdIsNull = profileRepository.getProfilesByParentIdIsNull();
        Map<String,Object> data = new HashMap<>();
        data.put("profiles",profiles);
        data.put("profilesParentIdIsNull",profilesParentIdIsNull);

        return ResponseEntity.ok(data);
    }
}
