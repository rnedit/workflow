package kz.spring.workflow.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
//@Controller
class MyErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // TODO: log error details here

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // display specific error page
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                log.error("HttpStatus.NOT_FOUND "+"redirect:/");
                return "redirect:/";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log.error("HttpStatus.INTERNAL_SERVER_ERROR "+"500");
                return "500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                log.error("HttpStatus.FORBIDDEN "+"redirect:/");
                return "redirect:/";
            } else if(statusCode == HttpStatus.UNAUTHORIZED.value()) {
                log.error("HttpStatus.UNAUTHORIZED "+"redirect:/login"
                );
                return "redirect:/login";
            } else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                log.error("HttpStatus.BAD_REQUEST "+"400");
                return "400";
            }
        }

        // display generic error
        return "error";
    }
}
