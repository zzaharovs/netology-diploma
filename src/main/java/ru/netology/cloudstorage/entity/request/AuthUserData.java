package ru.netology.cloudstorage.entity.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
public class AuthUserData {

    @NotBlank
    private String login;
    @NotBlank
    private String password;

}
