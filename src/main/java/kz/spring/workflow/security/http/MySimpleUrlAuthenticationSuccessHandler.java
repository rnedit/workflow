package kz.spring.workflow.security.http;

import kz.spring.workflow.domain.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@Component("myAuthenticationSuccessHandler")
public class MySimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());


    ActiveUserStore activeUserStore;

    public MySimpleUrlAuthenticationSuccessHandler() {
        super();
        logger.info("super onAuthenticationSuccess");
        setUseReferer(true);
    }


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
        logger.info("onAuthenticationSuccess");
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.setMaxInactiveInterval(30 * 60);

            String username;
            if (authentication.getPrincipal() instanceof User) {
                username = ((User)authentication.getPrincipal()).getEmail();
            }
            else {
                username = authentication.getName();
            }
            LoggedUser user = new LoggedUser(username, activeUserStore);
            session.setAttribute("user", user);
            logger.info(user.getUsername());
        }
        clearAuthenticationAttributes(request);

       // loginNotification(authentication, request);
    }

 /*
    private void loginNotification(Authentication authentication, HttpServletRequest request) {
        try {
            if (authentication.getPrincipal() instanceof User) {
                deviceService.verifyDevice(((User)authentication.getPrincipal()), request);
            }
        } catch (Exception e) {
           // logger.error("An error occurred while verifying device or location", e);
            throw new RuntimeException(e);
        }

    }



    protected void clearAuthenticationAttributes(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

  */
}
