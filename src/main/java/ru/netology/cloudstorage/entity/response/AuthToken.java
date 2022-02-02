package ru.netology.cloudstorage.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthToken {

    @JsonProperty("auth-token")
    private String authToken;

}
