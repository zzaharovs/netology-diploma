package ru.netology.cloudstorage.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudstorage.entity.security.AuthUserData;
import ru.netology.cloudstorage.entity.response.AuthToken;
import ru.netology.cloudstorage.security.CloudAuthService;

import javax.validation.constraints.NotBlank;

@RestController
@CrossOrigin(
        origins = {"http://localhost:8080"},
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS})
@AllArgsConstructor
@Validated
public class CloudAuthController {

    private final CloudAuthService authService;

    @PostMapping("/login")
    public AuthToken login(@RequestBody AuthUserData userData) {
        final String token = authService.getAuthTokenByUsernameAndPassword(userData.getLogin(), userData.getPassword());
        return new AuthToken(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@RequestHeader("auth-token") @NotBlank String token) {
        return new ResponseEntity<>(authService.deleteTokenAndLogout(token));
    }


}
