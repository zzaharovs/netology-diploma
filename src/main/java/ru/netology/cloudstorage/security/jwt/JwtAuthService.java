package ru.netology.cloudstorage.security.jwt;


public interface JwtAuthService {

    public String generateToken(String username);

    public boolean validateToken(String token);

}
