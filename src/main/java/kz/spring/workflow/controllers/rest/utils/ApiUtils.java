package kz.spring.workflow.controllers.rest.utils;

import kz.spring.workflow.domain.ERole;
import kz.spring.workflow.domain.Role;
import kz.spring.workflow.repository.RoleRepository;
import kz.spring.workflow.repository.UserRepository;
import kz.spring.workflow.service.DAL.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static kz.spring.workflow.domain.ERole.*;

@Service
public class ApiUtils {

    final
    RoleRepository roleRepository;

    public ApiUtils(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Role> calcRoles(Set<Role> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            if (!strRoles.isEmpty()) {
                strRoles.forEach(role ->{
                    switch (role.getName()) {
                        case ROLE_ADMIN:
                            Role adminRole = roleRepository.findByName(ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case ROLE_MODERATOR:
                            Role modRole = roleRepository.findByName(ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);

                            break;
                        case ROLE_USER:
                            Role usrRole = roleRepository.findByName(ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(usrRole);

                            break;
                        case ROLE_ANONYMOUS:
                            Role anaRole = roleRepository.findByName(ROLE_ANONYMOUS)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(anaRole);

                            break;
                    }
                });
            } else {
                Role userRole = roleRepository.findByName(ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);

            }

        }
        return roles;
    }
}
