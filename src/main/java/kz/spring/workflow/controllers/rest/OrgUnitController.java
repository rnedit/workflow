package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.OrgUnit;

import kz.spring.workflow.domain.Profile;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.repository.OrgUnitRepository;
import kz.spring.workflow.repository.ProfileRepository;
import kz.spring.workflow.request.OrgUnitsRequest;
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
    @Autowired
    OrgUnitRepository orgUnitRepository;
    @Autowired
    ProfileRepository profileRepository;

    @PostMapping()
    public ResponseEntity<?> orgunits(@Valid @RequestBody UsersRequest usersRequest,
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
            error.put("ERROR","ProfileRequest null");
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
            error.put("ERROR","ProfileRequest null");
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
        Set<Profile> profiles = orgUnit.getProfiles();
        profiles.forEach(profile -> {
            profile.setOrgUnit(null);
            profileRepository.save(profile);
        });

        Map<String,String> inf = new HashMap<>();
        inf.put("SUCCESS","Profile Deleted");
        inf.put("code","0");
        return ResponseEntity.ok(inf);
    }

}
