package ru.netology.cloudstorage.security.jwt;


import org.springframework.http.HttpStatus;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;

public interface JwtAuthService {

    String generateToken(String secret);

    String saveTokenToDataBase(String token, String username);

    boolean validateToken(String token, String username);

    HttpStatus deleteToken(String token);

    UserJwtEntity getUserByToken(String token);



}
