package kz.spring.workflow.controllers.rest.utils;

import kz.spring.workflow.domain.ERole;
import kz.spring.workflow.domain.Role;
import kz.spring.workflow.repository.RoleRepository;
import kz.spring.workflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ApiUtils {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public Set<Role> calcRoles(Set<Role> strRoles) {
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            if (!strRoles.isEmpty()) {
                strRoles.forEach(role ->{
                    switch (role.getName()) {
                        case ROLE_ADMIN:
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case ROLE_MODERATOR:
                            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);

                            break;
                        case ROLE_USER:
                            Role usrRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(usrRole);

                            break;
                        case ROLE_ANONYMOUS:
                            Role anaRole = roleRepository.findByName(ERole.ROLE_ANONYMOUS)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(anaRole);

                            break;
                    }
                });
            } else {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);

            }

        }
        return roles;
    }
}
