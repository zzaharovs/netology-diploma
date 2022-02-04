package ru.netology.cloudstorage.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;
import ru.netology.cloudstorage.entity.exception.InvalidJwtException;
import ru.netology.cloudstorage.repo.CloudJwtSecurityRepo;
import ru.netology.cloudstorage.security.jwt.JwtAuthServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CloudTokenAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private CloudJwtSecurityRepo jwtSecurityRepo;
    @Autowired
    private CloudUserDetailsService userDetailsService;
    @Autowired
    private JwtAuthServiceImpl jwtAuthService;

    public static final String JWT_REQUEST_HEADER = "auth-token";


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String token = httpRequest.getHeader(JWT_REQUEST_HEADER);

        if (token != null) {
            UserJwtEntity userJwt = jwtSecurityRepo.findDistinctByJwtToken(token.substring(7))
                    .orElseThrow(() -> new InvalidJwtException("User haven't valid jwt-token"));
            if (jwtAuthService.validateToken(userJwt.getJwtToken(), userJwt.getUsername())) {
                UserDetails currentUser = userDetailsService.loadUserByUsername(userJwt.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }


}
