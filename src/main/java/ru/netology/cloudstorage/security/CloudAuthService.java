package ru.netology.cloudstorage.security;

import org.springframework.http.HttpStatus;

public interface CloudAuthService {

    String getAuthTokenByUsernameAndPassword(String username, String password);

    HttpStatus deleteTokenAndLogout(String token);


}
