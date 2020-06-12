package kz.spring.workflow.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

}
