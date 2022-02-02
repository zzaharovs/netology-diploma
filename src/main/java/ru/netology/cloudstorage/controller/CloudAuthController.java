package ru.netology.cloudstorage.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudstorage.entity.security.AuthUserData;
import ru.netology.cloudstorage.entity.response.AuthToken;
import ru.netology.cloudstorage.security.CloudAuthService;

@RestController
@CrossOrigin(origins = {"http://localhost:8080/","http://localhost:8787"}, allowCredentials = "true",
        allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@AllArgsConstructor
public class CloudAuthController {

    private final CloudAuthService authService;

    @PostMapping("/login")
    public AuthToken login(@RequestBody AuthUserData userData)
    {
        final String token = authService.getAuthTokenByUsernameAndPassword(userData.getLogin(), userData.getPassword());
//        System.out.println(userData.getLogin());
//        System.out.println(userData.getPassword());
//        token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY0MzU0ODcwNCwiaWF0IjoxNjQzNTQ4NzA0fQ.KZGKelwZMJtppCv8HVFaHcSwvmn5VIvteYN7Enu_OUA";
        return new AuthToken(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout()
    {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
