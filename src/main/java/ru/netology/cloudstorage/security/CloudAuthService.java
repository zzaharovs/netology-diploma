package ru.netology.cloudstorage.security;

public interface CloudAuthService {

    String getAuthTokenByUsernameAndPassword(String username, String password);


}
