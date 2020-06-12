package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.ERole;
import kz.spring.workflow.domain.Role;
import kz.spring.workflow.domain.User;
import kz.spring.workflow.repository.RoleRepository;
import kz.spring.workflow.repository.UserRepository;
import kz.spring.workflow.request.RefreshJwt;
import kz.spring.workflow.request.LoginRequest;
import kz.spring.workflow.request.SignupRequest;
import kz.spring.workflow.response.JwtResponse;
import kz.spring.workflow.response.MessageResponse;
import kz.spring.workflow.security.jwt.AuthTokenFilter;
import kz.spring.workflow.security.jwt.JwtUtils;
import kz.spring.workflow.security.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping()
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${cookie.maxAge}")
    private int cookieMaxAgeS;

    @Value("${refreshJwt.maxAge}")
    private int refreshJwtmaxAge;


    @PostMapping("/api/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                   HttpServletResponse response) {
        Map<String,String> error = new HashMap<>();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = jwtUtils.generateJwtToken(authentication);
        Boolean loggedIn = false;
        if (jwt!=null) loggedIn = true;

       User user = userRepository.getByUsername(loginRequest.getUsername());
       final String jwtUserRefresh = jwtUtils.createRefreshToken();

        user.setRefreshJwt(jwtUserRefresh);
        userRepository.save(user);

        List<ERole> roles = new ArrayList<>();
        user.getRoles().iterator().forEachRemaining(role -> {
            roles.add(role.getName());
        });

        //Отправка кук пользователю для дальнейших запросов от фронтенда, нужна следующая конфигурация на бакенде
        //все запросы от клиента должны быть {withCredentials: true}
        /* WebSecurityConfig.java
        @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
                registry.addMapping("/**").allowCredentials(true);
            }
        };
    }
         */
        Cookie cookie = new Cookie("jwtID", jwt);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieMaxAgeS);
        response.addCookie(cookie);

        Date updatedDate = new Date();
        return ResponseEntity.ok(new JwtResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getName(),
                loggedIn,
                jwtUserRefresh,
                user.getRefreshJwtMaxAge(),
                updatedDate,
                roles
        ));

       // return "redirect:/";
    }

    @PostMapping("/api/auth/refreshjwt")
    public ResponseEntity<?> refreshJWT(@Valid @RequestBody RefreshJwt refreshJwt,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        Map<String,String> error = new HashMap<>();
        String newJwt = null;

        if (refreshJwt.getRefreshJwt() != null) {

            User user = userRepository.getByRefreshJwt(refreshJwt.getRefreshJwt());
            if (user!=null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
                        userDetails.getAuthorities());
                newJwt = jwtUtils.generateJwtToken(authentication);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                final String jwtUserRefresh = jwtUtils.createRefreshToken();
                user.setRefreshJwt(jwtUserRefresh);
                userRepository.save(user);

                List<ERole> roles = new ArrayList<>();
                user.getRoles().iterator().forEachRemaining(role -> {
                    roles.add(role.getName());
                });

                Boolean loggedIn = false;
                if (newJwt!=null) loggedIn = true;
                Cookie cookie = new Cookie("jwtID", newJwt);
                cookie.setPath("/");
                cookie.setSecure(true);
                cookie.setHttpOnly(true);
                cookie.setMaxAge(cookieMaxAgeS);
                response.addCookie(cookie);

                Date updatedDate = new Date();
                return ResponseEntity.ok(new JwtResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getName(),
                        loggedIn,
                        jwtUserRefresh,
                        user.getRefreshJwtMaxAge(),
                        updatedDate,
                        roles
                ));
            }
        }
        error.put("ERROR","jwt error");
        error.put("code","0");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        // return "redirect:/";
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        Map<String,String> error = new HashMap<>();
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            error.put("ERROR","User Name Exist");
            error.put("code","0");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                 //   .badRequest()
                  //  .body(new MessageResponse("Error: Username is already User Name!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            error.put("ERROR","Email Exist");
            error.put("code","1");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // Create new user's account
        User user = null;
            user = new User(
                    signUpRequest.getUsername(),
                    signUpRequest.getName(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getFirstName(),
                    signUpRequest.getLastName(),
                    //jwtUtils.createRefreshToken(),
                    refreshJwtmaxAge
            );


        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            if (!strRoles.isEmpty()) {
                strRoles.forEach(role ->{
                    switch (role) {
                        case "ADMIN":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case "MODERATOR":
                            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);

                            break;
                        case "USER":
                            Role usrRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(usrRole);

                            break;
                        case "ANONYMOUS":
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

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
