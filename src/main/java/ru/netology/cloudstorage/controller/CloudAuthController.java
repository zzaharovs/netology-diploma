package ru.netology.cloudstorage.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudstorage.entity.request.AuthUserData;
import ru.netology.cloudstorage.entity.response.AuthToken;

@RestController
@CrossOrigin(origins = {"http://localhost:8080/","http://localhost:8787"}, allowCredentials = "true",
        allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class CloudAuthController {

    private String token;

    @PostMapping("/login")
    public AuthToken login(@RequestBody AuthUserData userData)
    {
        token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY0MzU0ODcwNCwiaWF0IjoxNjQzNTQ4NzA0fQ.KZGKelwZMJtppCv8HVFaHcSwvmn5VIvteYN7Enu_OUA";
        return new AuthToken(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout()
    {
        token = null;
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
