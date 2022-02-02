package ru.netology.cloudstorage.entity.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netology.cloudstorage.entity.db.UserEntity;

import javax.validation.constraints.NotBlank;
import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
public class AuthUserData {

    @NotBlank
    private String login;
    @NotBlank
    private String password;

    public AuthUserData(UserEntity entity) {
        this.login = entity.getUsername();
        this.password = entity.getPassword();
    }
}
