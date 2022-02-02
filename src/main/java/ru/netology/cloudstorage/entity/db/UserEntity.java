package ru.netology.cloudstorage.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @OneToMany(mappedBy = "username")
    private List<FileInfoEntity> files;

//    public UserEntity(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }
}
