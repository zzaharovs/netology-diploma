package ru.netology.cloudstorage.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "username", fetch = FetchType.LAZY)
    private List<FileInfoEntity> files;
}
