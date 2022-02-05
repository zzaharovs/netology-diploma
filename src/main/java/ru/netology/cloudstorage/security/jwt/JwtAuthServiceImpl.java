package ru.netology.cloudstorage.security.jwt;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;
import ru.netology.cloudstorage.entity.exception.InvalidJwtException;
import ru.netology.cloudstorage.repo.CloudJwtSecurityRepo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class JwtAuthServiceImpl implements JwtAuthService {

    private final UserDetailsService userDetailsService;
    private final CloudJwtSecurityRepo jwtSecurityRepo;

    @Override
    public String generateToken(String secret) {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now().plusMinutes(30)
                .atZone(ZoneId.systemDefault()).toInstant());

        String token = "";
        try {
            token = Jwts.builder()
                    .setId(id)
                    .setIssuedAt(now)
                    .setNotBefore(now)
                    .setExpiration(exp)
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public String saveTokenToDataBase(String token, String username) {
        jwtSecurityRepo.save(new UserJwtEntity(token, username));
        log.info("Токен для пользователя {} сгенерирован и успешно сохранен в базу", username );
        return token;
    }

    @Override
    public boolean validateToken(String token, String username) {

        UserDetails user = userDetailsService.loadUserByUsername(username);
        try {
            Jwts.parser().setSigningKey(user.getPassword() + user.getUsername()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            jwtSecurityRepo.deleteById(token);
            throw new InvalidJwtException("Token expired");
        } catch (Exception e) {
            jwtSecurityRepo.deleteById(token);
            throw new InvalidJwtException("Invalid token");
        }
    }

    @Override
    public HttpStatus deleteToken(String token) {
        jwtSecurityRepo.deleteById(token);
        log.info("Токен успешно удален из базы");
        return HttpStatus.OK;
    }

    @Override
    public UserJwtEntity getUserByToken(String token) {
        return jwtSecurityRepo.findDistinctByJwtToken(token)
                .orElseThrow(() -> new InvalidJwtException("User haven't jwt-token"));
    }


}
