package ru.netology.cloudstorage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.netology.cloudstorage.entity.db.FileInfoEntity;
import ru.netology.cloudstorage.entity.db.FileInfoKey;

import javax.transaction.Transactional;
import java.util.List;

public interface CloudFilesRepo extends JpaRepository<FileInfoEntity, FileInfoKey> {

    List<FileInfoEntity> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("update FileInfoEntity f set f.fileName = :newName" +
            " where f.username = :username and f.fileName = :oldName")
    void editFileName(@Param("username") String username,
                      @Param("oldName") String currentFilename,
                      @Param("newName") String newFileName
    );



}
