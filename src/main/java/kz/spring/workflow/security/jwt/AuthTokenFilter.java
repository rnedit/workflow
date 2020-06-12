package kz.spring.workflow.security.jwt;

import kz.spring.workflow.security.services.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = null;
                try {
                    username = jwtUtils.getUserNameFromJwtToken(jwt);
                } catch (IllegalArgumentException e) {
                    logger.error("an error occured during getting username from token", e);
                }
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {

        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null) {
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("token ")) {
                return headerAuth.substring(6, headerAuth.length());
            }
        } else {
            String jwtCookie = null;
            if (request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if (c.getName().equals("jwtID")) {
                        jwtCookie = c.getValue();
                        break;
                    }
                }
            }
            if (jwtCookie != null) {
                return jwtCookie;
            }
        }
        return null;
    }
}
