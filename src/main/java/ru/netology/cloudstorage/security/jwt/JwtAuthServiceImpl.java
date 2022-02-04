package ru.netology.cloudstorage.security.jwt;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.entity.db.UserEntity;
import ru.netology.cloudstorage.entity.exception.UserNotFoundException;
import ru.netology.cloudstorage.repo.CloudSecurityRepo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class JwtAuthServiceImpl implements JwtAuthService {

    private final CloudSecurityRepo securityRepo;

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
            //ignore
        }
        return token;
    }

    @Override
    public boolean validateToken(String token, String username) {

        UserEntity user = securityRepo.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            Jwts.parser().setSigningKey(user.getPassword() + user.getUsername()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            System.out.println("ex1");
//            log.severe("Token expired");
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            log.severe("invalid token");
        }
        return false;
    }


}
