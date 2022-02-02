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
    private String username;
    @Column(nullable = false)
    private byte[] file;
    @Column(nullable = false)
    private Long size;

}
