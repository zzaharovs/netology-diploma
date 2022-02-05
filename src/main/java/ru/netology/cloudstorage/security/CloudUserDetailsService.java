package ru.netology.cloudstorage.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

        UserEntity currentUser = securityRepo.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(currentUser.getUsername(), currentUser.getPassword(), roles);
    }

    public String getUsernameByContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
