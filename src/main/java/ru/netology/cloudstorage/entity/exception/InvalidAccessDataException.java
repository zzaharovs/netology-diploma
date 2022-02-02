package ru.netology.cloudstorage.entity.exception;

public class InvalidAccessDataException extends RuntimeException {

    public InvalidAccessDataException(String message) {
        super(message);
    }
}
