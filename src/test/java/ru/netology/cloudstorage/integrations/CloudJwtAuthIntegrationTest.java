package ru.netology.cloudstorage.integrations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;
import ru.netology.cloudstorage.entity.exception.InvalidJwtException;
import ru.netology.cloudstorage.entity.exception.UserNotFoundException;
import ru.netology.cloudstorage.repo.CloudJwtSecurityRepo;
import ru.netology.cloudstorage.security.filters.CloudTokenAuthenticationFilter;
import ru.netology.cloudstorage.security.jwt.JwtAuthServiceImpl;

import javax.servlet.ServletException;
import java.io.IOException;

import static ru.netology.cloudstorage.security.filters.CloudTokenAuthenticationFilter.JWT_REQUEST_HEADER;


@SpringBootTest
@Testcontainers
public class CloudJwtAuthIntegrationTest {

    @Autowired
    private CloudTokenAuthenticationFilter authenticationFilter;
    @Autowired
    private JwtAuthServiceImpl jwtAuthService;
    @Autowired
    private CloudJwtSecurityRepo jwtSecurityRepo;

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
    public void doFilterIntegrationSuccessTest() throws IOException, ServletException {
        //given
        final String username = "demo_user1";
        final String password = "pass1";
        final String dbToken = jwtAuthService.generateToken(password + username);
        final String headerToken = String.format("Bearer %s", dbToken);
        jwtSecurityRepo.save(new UserJwtEntity(dbToken, username));
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWT_REQUEST_HEADER, headerToken);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain mockFilterChain = new MockFilterChain();
        //when
        authenticationFilter.doFilter(request, response, mockFilterChain);
        final String actualResult = SecurityContextHolder.getContext().getAuthentication().getName();
        //then
        Assertions.assertEquals(username, actualResult);
    }

    @Test
    @Transactional
    public void doFilterIntegrationUserHaveNotJwtExceptionTest() throws IOException, ServletException {
        //given
        final String username = "demo_user1";
        final String password = "pass1";
        final String dbToken = jwtAuthService.generateToken(password + username);
        final String headerToken = String.format("Bearer %s", dbToken);
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWT_REQUEST_HEADER, headerToken);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain mockFilterChain = new MockFilterChain();
        //when
        InvalidJwtException thrown = Assertions.assertThrows(InvalidJwtException.class, () -> {
            authenticationFilter.doFilter(request, response, mockFilterChain);
        });
        //then
        Assertions.assertEquals("User haven't jwt-token", thrown.getMessage());
    }


    @Test
    @Transactional
    public void doFilterIntegrationJwtValidationExceptionTest() throws IOException, ServletException {
        //given
        final String username = "demo_user1";
        final String invalidSecret = "invalid_secret";
        final String dbToken = jwtAuthService.generateToken(invalidSecret);
        final String headerToken = String.format("Bearer %s", dbToken);
        jwtSecurityRepo.save(new UserJwtEntity(dbToken, username));
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWT_REQUEST_HEADER, headerToken);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain mockFilterChain = new MockFilterChain();
        //when
        InvalidJwtException thrown = Assertions.assertThrows(InvalidJwtException.class, () -> {
            authenticationFilter.doFilter(request, response, mockFilterChain);
        });
        //then
        Assertions.assertEquals("Invalid token", thrown.getMessage());
    }

    @Test
    @Transactional
    public void doFilterIntegrationUSerNotFoundExceptionTest() throws IOException, ServletException {
        //given
        final String username = "demo_user1";
        final String invalidUser = "invalidUser";
        final String password = "pass1";
        final String dbToken = jwtAuthService.generateToken(password + username);
        final String headerToken = String.format("Bearer %s", dbToken);
        jwtSecurityRepo.save(new UserJwtEntity(dbToken, invalidUser));
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWT_REQUEST_HEADER, headerToken);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final MockFilterChain mockFilterChain = new MockFilterChain();
        //when
        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class, () -> {
            authenticationFilter.doFilter(request, response, mockFilterChain);
        });
        //then
        Assertions.assertEquals("User not found", thrown.getMessage());
    }



}