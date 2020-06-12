package kz.spring.workflow.security.http;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Component("myLogoutSuccessHandler")
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        final HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("user");
        }
        logger.info("logSUCC");
        //response.sendRedirect("/");
    }
}
