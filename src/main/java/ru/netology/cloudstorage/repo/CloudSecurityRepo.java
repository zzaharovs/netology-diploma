package ru.netology.cloudstorage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.cloudstorage.entity.db.UserEntity;

public interface CloudSecurityRepo extends JpaRepository<UserEntity, String> {



}
