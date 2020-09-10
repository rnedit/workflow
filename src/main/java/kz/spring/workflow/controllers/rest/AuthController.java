package kz.spring.workflow.controllers.rest;

import kz.spring.workflow.domain.*;
import kz.spring.workflow.repository.ProfileRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import kz.spring.workflow.controllers.rest.utils.*;


@RestController
@RequestMapping()
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ApiUtils apiUtils;

    @Value("${cookie.maxAge}")
    private int cookieMaxAgeS;

    @Value("${refreshJwt.maxAge}")
    private int refreshJwtmaxAge;


    @PostMapping("/api/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                   HttpServletResponse response,BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","signin null");
            error.put("code","1");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
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

        Profile profile = profileRepository.getById(user.getParentIdProfile());

        if (profile==null) {
            profile = new Profile("Профайла нет");
            profile.setId("0");
        }

        List<ERole> roles = new ArrayList<>();
        user.getRoles().iterator().forEachRemaining(role -> {
            roles.add(role.getName());
        });

        Set<String> rolesId = new HashSet<>();
        user.getRoles().iterator().forEachRemaining(role -> {
            rolesId.add(role.getId());
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
        cookie.setSecure(false); //<---------------------------------------------
        cookie.setHttpOnly(true);
        cookie.setMaxAge(cookieMaxAgeS);
        response.addCookie(cookie);

        Date updatedDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(updatedDate);
        cal.add(Calendar.SECOND, refreshJwtmaxAge);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String ff;
        if (user.getCreationDate()!=null)
            ff = formatter.format(user.getCreationDate());
        else
            ff = "null";
        return ResponseEntity.ok(new JwtResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getName(),
                loggedIn,
                jwtUserRefresh,
                user.getRefreshJwtMaxAge(),
                cal.getTime(),
                roles,
                rolesId,
                ff,
                user.getEditable()!=null?user.getEditable():true,
                profile
        ));

       // return "redirect:/";
    }

    @PostMapping("/api/auth/refreshjwt")
    public ResponseEntity<?> refreshJWT(@Valid @RequestBody RefreshJwt refreshJwt,
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        BindingResult bindingResult) {

        Map<String,String> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error.put("ERROR","jwt null");
            error.put("code","1");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
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

                Profile profile = profileRepository.getById(user.getParentIdProfile());

                if (profile==null) {
                    profile = new Profile("Профайла нет");
                    profile.setId("0");
                }

                List<ERole> roles = new ArrayList<>();
                user.getRoles().iterator().forEachRemaining(role -> {
                    roles.add(role.getName());
                });

                Set<String> rolesId = new HashSet<>();
                user.getRoles().iterator().forEachRemaining(role -> {
                    rolesId.add(role.getId());
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
                Calendar cal = Calendar.getInstance();
                cal.setTime(updatedDate);
                cal.add(Calendar.SECOND, refreshJwtmaxAge);

                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                String ff;
                if (user.getCreationDate()!=null)
                    ff = formatter.format(user.getCreationDate());
                else
                    ff = "null";
                return ResponseEntity.ok(new JwtResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getName(),
                        loggedIn,
                        jwtUserRefresh,
                        user.getRefreshJwtMaxAge(),
                        cal.getTime(),
                        roles,
                        rolesId,
                        ff,
                        user.getEditable()!=null?user.getEditable():true,
                        profile
                ));
            } else {
                error.put("ERROR","User null id refreshJwt = " +refreshJwt.getRefreshJwt());
                error.put("code","0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
        }
        error.put("ERROR","jwt error");
        error.put("code","0");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        // return "redirect:/";
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                          BindingResult bindingResult) {
        Map<String,String> error = new HashMap<>();

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e->{
                System.out.println(e.getObjectName() +" "+ e.getCode());
                System.out.println(String.valueOf(signUpRequest));
            });
            error.put("ERROR","signup null");
            error.put("code","2");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

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
        User   user = new User(
                    signUpRequest.getUsername(),
                    signUpRequest.getName(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getFirstName(),
                    signUpRequest.getLastName(),

                    //jwtUtils.createRefreshToken(),
                    refreshJwtmaxAge
            );


        user.setRoles(apiUtils.calcRoles(signUpRequest.getRoles()));
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


}
