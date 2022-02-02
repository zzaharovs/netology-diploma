package ru.netology.cloudstorage.entity.db;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FileInfoKey.class)
public class FileInfoEntity {

    @Id
    private String fileName;
    @Id
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "username")
//    private UserEntity username;
    private String username;
//    @Lob
    @Column(nullable = false)
    private byte[] file;
    @Column(nullable = false)
    private Long size;

}
