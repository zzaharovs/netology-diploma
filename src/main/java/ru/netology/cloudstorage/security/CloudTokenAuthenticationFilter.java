package ru.netology.cloudstorage.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class CloudTokenAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private CloudJwtSecurityRepo jwtSecurityRepo;
    @Autowired
    private CloudUserDetailsService userDetailsService;
    @Autowired
    private JwtAuthServiceImpl jwtAuthService;

//    public CloudTokenAuthenticationFilter(AuthenticationManager authenticationManager, CloudJwtSecurityRepo jwtSecurityRepo, CloudUserDetailsService userDetailsService, JwtAuthServiceImpl jwtAuthService) {
//        super(authenticationManager);
//        this.jwtSecurityRepo = jwtSecurityRepo;
//        this.userDetailsService = userDetailsService;
//        this.jwtAuthService = jwtAuthService;
//    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        final Optional<String> headerToken = Optional.ofNullable(httpRequest.getHeader("auth-token"));
        final String token = headerToken.orElse("based");//Throw(() -> new InvalidJwtException("Request doesn't contain token"));
        final UserJwtEntity userJwt;

        System.out.println("222");

        if (token.startsWith("Bearer ")) {
            userJwt = jwtSecurityRepo.findDistinctByJwtToken(token.substring(7));
            if (jwtAuthService.validateToken(userJwt.getJwtToken())) {
                UserDetails currentUser = userDetailsService.loadUserByUsername(userJwt.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }


}
