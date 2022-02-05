package ru.netology.cloudstorage.integrations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;
import ru.netology.cloudstorage.repo.CloudJwtSecurityRepo;
import ru.netology.cloudstorage.security.CloudAuthService;
import ru.netology.cloudstorage.security.jwt.JwtAuthService;

import java.util.Optional;


@SpringBootTest
@Testcontainers
public class CloudBasicAuthIntegrationTest {

    @Autowired
    private CloudAuthService authService;
    @Autowired
    private CloudJwtSecurityRepo jwtSecurityRepo;
    @Autowired
    private JwtAuthService jwtService;

    @Container
    public static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("db")
            .withUsername("postgres")
            .withPassword("pass");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",
                () -> String.format("jdbc:postgresql://localhost:%d/db", postgresSQLContainer.getFirstMappedPort()));
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "pass");
        registry.add("spring.liquibase.enabled", () -> true);
    }


    @Test
    @Transactional
    public void loginIntegrationSuccessTest() {

        //given
        final String username = "demo_user1";
        final String password = "pass1";
        //when
        final String apiToken = authService.getAuthTokenByUsernameAndPassword(username, password);
        final UserJwtEntity user = jwtSecurityRepo.getById(apiToken);
        //then
        Assertions.assertEquals(new UserJwtEntity(apiToken, username), user);
    }

    @Test
    @Transactional
    public void deleteTokenAndLogoutSuccessCase() {
        //given
        final String username = "demo_user1";
        final String password = "pass1";
        final String dbToken = jwtService.generateToken(password + username);
        final String headerToken = String.format("Bearer %s", dbToken);
        jwtSecurityRepo.save(new UserJwtEntity(dbToken, password));
        //when
        authService.deleteTokenAndLogout(headerToken);
        Optional<UserJwtEntity> expected = jwtSecurityRepo.findDistinctByJwtToken(dbToken);
        //then
        Assertions.assertFalse(expected.isPresent());
    }


}

