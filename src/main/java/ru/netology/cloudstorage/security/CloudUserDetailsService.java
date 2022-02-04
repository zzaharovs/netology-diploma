package ru.netology.cloudstorage.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netology.cloudstorage.entity.db.UserEntity;
import ru.netology.cloudstorage.entity.exception.UserNotFoundException;
import ru.netology.cloudstorage.repo.CloudSecurityRepo;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CloudUserDetailsService implements UserDetailsService {

    private final CloudSecurityRepo securityRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Поиск пользователя в базе по указанному логину {}", username);
        UserEntity currentUser = securityRepo.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User not found. Contact your system administrator"));
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        User user = new User(currentUser.getUsername(), currentUser.getPassword(), roles);
        log.info("Аутентификация успешна");
        return user;
    }
}
