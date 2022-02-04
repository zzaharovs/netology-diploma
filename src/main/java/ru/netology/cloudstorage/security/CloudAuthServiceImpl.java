package ru.netology.cloudstorage.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;
import ru.netology.cloudstorage.entity.exception.InvalidAccessDataException;
import ru.netology.cloudstorage.repo.CloudJwtSecurityRepo;
import ru.netology.cloudstorage.security.jwt.JwtAuthService;

@AllArgsConstructor
@Service
@Slf4j
public class CloudAuthServiceImpl implements CloudAuthService {

    private final JwtAuthService service;
    private final UserDetailsService userDetailsService;
    private final CloudJwtSecurityRepo jwtSecurityRepo;

    @Override
    public String getAuthTokenByUsernameAndPassword(String username, String password) {
        if (validateUser(username, password)) {
            String token = service.generateToken(password + username);
            return saveTokenToDatabase(token, username);
        }
        throw new InvalidAccessDataException("invalid username or password");
    }

    @Override
    public HttpStatus deleteTokenAndLogout(String token) {
        jwtSecurityRepo.deleteById(token.substring(7));
        log.info("Токен успешно удален из базы");
        return HttpStatus.OK;
    }

    private boolean validateUser(String username, String password) {
        UserDetails currentUser = userDetailsService.loadUserByUsername(username);
        return username.equals(currentUser.getUsername()) && password.equals(currentUser.getPassword());
    }

    private String saveTokenToDatabase(String token, String username) {
        jwtSecurityRepo.save(new UserJwtEntity(token, username));
        log.info("Токен для пользователя {} сгенерирован и успешно сохранен в базу", username );
        return token;
    }


}
