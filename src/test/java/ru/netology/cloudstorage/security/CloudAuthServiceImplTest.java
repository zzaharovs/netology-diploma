package ru.netology.cloudstorage.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.netology.cloudstorage.entity.db.UserEntity;
import ru.netology.cloudstorage.entity.exception.InvalidAccessDataException;
import ru.netology.cloudstorage.entity.exception.UserNotFoundException;
import ru.netology.cloudstorage.repo.CloudSecurityRepo;
import ru.netology.cloudstorage.security.jwt.JwtAuthService;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CloudAuthServiceImpl.class, CloudUserDetailsService.class})
public class CloudAuthServiceImplTest {

    @MockBean
    private JwtAuthService jwtAuthService;
    @MockBean
    private CloudSecurityRepo cloudSecurityRepo;
    @Autowired
    private CloudAuthServiceImpl authService;
    @Autowired
    private CloudUserDetailsService cloudUserDetailsService;

    @Test
    @WithMockUser("test_user")
    public void getAuthTokenByUsernameAndPasswordSuccessCaseTest() {
        //given
        final String username = "test_user";
        final String password = "password";
        final String tokenExpected = "test_jwt_token";
        Mockito.when(cloudSecurityRepo.findById(username)).thenReturn(Optional.of(new UserEntity(username, password)));
        Mockito.when(jwtAuthService.generateToken(password + username))
                .thenReturn(tokenExpected);
        Mockito.when(jwtAuthService.saveTokenToDataBase(tokenExpected, username))
                .thenReturn(tokenExpected);
        //when
        final String tokenActual = authService.getAuthTokenByUsernameAndPassword(username, password);
        //then
        Assertions.assertEquals(tokenExpected, tokenActual);

    }

    @Test
    @WithMockUser("test_user")
    public void getAuthTokenByUsernameAndPasswordThrowInvalidAccessTest() {
        //given
        final String username = "test_user";
        final String invalidPassword = "test_invalid_pass";
        final String validPass = "test_valid_pass";
        Mockito.when(cloudSecurityRepo.findById(username)).thenReturn(Optional.of(new UserEntity(username, validPass)));
        //when
        InvalidAccessDataException thrown = Assertions.assertThrows(InvalidAccessDataException.class, () -> {
            authService.getAuthTokenByUsernameAndPassword(username, invalidPassword);
        });
        //then
        Assertions.assertEquals("Invalid username or password", thrown.getMessage());
    }

    @Test
    public void getAuthTokenByUsernameAndPasswordThrowUsernameNotFoundTest() {
        //given
        final String invalidUsername = "test_invalid_user";
        final String password = "password";
        Mockito.when(cloudSecurityRepo.findById(invalidUsername)).thenReturn(Optional.empty());
        //when
        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class, () -> {
            authService.getAuthTokenByUsernameAndPassword(invalidUsername, password);
        });
        //then
        Assertions.assertEquals("User not found", thrown.getMessage());
    }


    @Test
    public void deleteTokenAndLogoutSuccessCaseTest() {
        //given
        final String token = "test_token_bearer";
        final HttpStatus expectedStatus = HttpStatus.OK;
        Mockito.when(jwtAuthService.deleteToken(token.substring(7)))
                .thenReturn(HttpStatus.OK);
        //when
        final HttpStatus actualStatus = authService.deleteTokenAndLogout(token);
        //then
        Assertions.assertEquals(expectedStatus, actualStatus);
    }
}
