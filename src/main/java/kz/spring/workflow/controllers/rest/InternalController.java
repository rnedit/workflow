package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.*;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.facades.internal.impl.InternalFacadeImpl;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import kz.spring.workflow.repository.UserRepository;
import kz.spring.workflow.request.InternalRequest;
import kz.spring.workflow.request.InternalTableRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/internals")
public class InternalController {

    @Autowired
    private InternalDALImpl internalDAL;

    @Autowired
    private InternalFacadeImpl internalFacade;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/getInternals")
    public ResponseEntity<?> getInternals(@Valid @RequestBody InternalTableRequest internalRequest,
                                       BindingResult bindingResult) {
        Map<String, String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR", "InternalRequest null");
            error.put("code", "2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        User user = userRepository.getById(internalRequest.getUserId());
        Pageable pageble = PageRequest.of(internalRequest.getPage() - 1, internalRequest.getPerPage());
        Map<String, Object> data = new HashMap<>();
        if (user.getRoles().contains(ERole.ROLE_USER)) {
            if (user.getParentIdProfile() == null) {
                error.put("ERROR", "getParentIdProfile null");
                error.put("code", "3");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            List<Internal> internalList = internalDAL.getAllMainOfAllReaders(user.getParentIdProfile(), pageble);
            data.put("internals", internalList);
            data.put("perpage", internalRequest.getPerPage());
            data.put("page", internalRequest.getPage());
            data.put("total", internalList.size());
            return ResponseEntity.ok(data);
        } else {
            Set<String> roles = new HashSet<>();
            user.getRoles().forEach(r -> {
                roles.add(r.getId());
            });
            List<Internal> internalList = internalDAL.getAllMainOfRoles(roles, pageble);
            data.put("internals", internalList);
            data.put("perpage", internalRequest.getPerPage());
            data.put("page", internalRequest.getPage());
            data.put("total", internalList.size());
            return ResponseEntity.ok(data);
        }
    }

    @PostMapping("/getInternal")
    public ResponseEntity<?> getInternal(@Valid @RequestBody InternalRequest internalRequest,
                                         BindingResult bindingResult) {
        Map<String, String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR", "getInternal id isEmpty or null");
            error.put("code", "2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
        return ResponseEntity.ok(internalFacade.getInternal(internalRequest.getId()));

    }
}
