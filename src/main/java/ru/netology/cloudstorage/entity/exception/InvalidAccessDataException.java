package ru.netology.cloudstorage.entity.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAccessDataException extends AuthenticationException {

    public InvalidAccessDataException(String message) {
        super(message);
    }
}
