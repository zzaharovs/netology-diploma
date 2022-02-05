package ru.netology.cloudstorage.security.filters;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;
import ru.netology.cloudstorage.security.CloudUserDetailsService;
import ru.netology.cloudstorage.security.jwt.JwtAuthServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class CloudTokenAuthenticationFilter extends GenericFilterBean {

    private final CloudUserDetailsService userDetailsService;
    private final JwtAuthServiceImpl jwtAuthService;

    public static final String JWT_REQUEST_HEADER = "auth-token";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String token = httpRequest.getHeader(JWT_REQUEST_HEADER);

        if (token != null) {
            log.info("Проверка наличия токена в базе и его валидности");
            UserJwtEntity userJwt = jwtAuthService.getUserByToken(token.substring(7));
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
