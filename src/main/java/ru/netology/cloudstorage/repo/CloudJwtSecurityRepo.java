package ru.netology.cloudstorage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudstorage.entity.db.UserJwtEntity;

import java.util.List;

public interface CloudJwtSecurityRepo extends JpaRepository<UserJwtEntity, String> {


    UserJwtEntity findDistinctByJwtToken(String jwtToken);

}
