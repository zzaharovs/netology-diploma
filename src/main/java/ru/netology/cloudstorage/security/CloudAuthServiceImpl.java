package ru.netology.cloudstorage.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;
import ru.netology.cloudstorage.entity.exception.InvalidAccessDataException;
import ru.netology.cloudstorage.repo.CloudJwtSecurityRepo;
import ru.netology.cloudstorage.security.jwt.JwtAuthService;

@AllArgsConstructor
@Service
public class CloudAuthServiceImpl implements CloudAuthService {

    private final JwtAuthService service;
    private final UserDetailsService userDetailsService;
    private final CloudJwtSecurityRepo jwtSecurityRepo;

    private static final String TOKEN_SECRET = "dummy";

    @Override
    public String getAuthTokenByUsernameAndPassword(String username, String password) {
        if (validateUser(username, password)) {
            String token = service.generateToken(password + TOKEN_SECRET + username);
            return saveTokenToDatabase(token, username);
        }
        throw new InvalidAccessDataException("invalid username or password");
    }

    private boolean validateUser (String username, String password) {
        UserDetails currentUser = userDetailsService.loadUserByUsername(username);
        return username.equals(currentUser.getUsername()) && password.equals(currentUser.getPassword());
    }

    private String saveTokenToDatabase(String token, String username){
        jwtSecurityRepo.save(new UserJwtEntity(token, username));
        return token;
    }



}
