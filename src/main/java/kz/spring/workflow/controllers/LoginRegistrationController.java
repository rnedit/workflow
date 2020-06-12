package kz.spring.workflow.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
@PreAuthorize("hasRole('ANONYMOUS')")
public class LoginRegistrationController {
    @GetMapping(value = {"login","registration"})
    public String loginRegistration() {


        return "loginRegistration";
    }
}
