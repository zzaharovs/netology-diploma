package ru.netology.cloudstorage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;

import java.util.Optional;

public interface CloudJwtSecurityRepo extends JpaRepository<UserJwtEntity, String> {

    Optional<UserJwtEntity> findDistinctByJwtToken(String jwtToken);

}
