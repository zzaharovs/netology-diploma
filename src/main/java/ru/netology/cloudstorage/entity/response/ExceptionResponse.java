package ru.netology.cloudstorage.entity.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionResponse {

    private String message;

    private int id;

}
