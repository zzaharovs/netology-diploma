package ru.netology.cloudstorage.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.entity.exception.InvalidAccessDataException;
import ru.netology.cloudstorage.security.jwt.JwtAuthService;

@AllArgsConstructor
@Service
@Slf4j
public class CloudAuthServiceImpl implements CloudAuthService {

    private final JwtAuthService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public String getAuthTokenByUsernameAndPassword(String username, String password) {
        if (validateUser(username, password)) {
            String token = jwtService.generateToken(password + username);
            return jwtService.saveTokenToDataBase(token, username);
        }
        throw new InvalidAccessDataException("Invalid username or password");
    }

    @Override
    public HttpStatus deleteTokenAndLogout(String token) {
        return jwtService.deleteToken(token.substring(7));
    }

    private boolean validateUser(String username, String password) {
        log.info("Поиск пользователя в базе по указанному логину {}", username);
        UserDetails currentUser = userDetailsService.loadUserByUsername(username);
        return username.equals(currentUser.getUsername()) && password.equals(currentUser.getPassword());
    }

}
