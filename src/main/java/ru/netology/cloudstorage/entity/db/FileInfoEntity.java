package ru.netology.cloudstorage.entity.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class FileInfoEntity {


    @EmbeddedId
    private FileInfoKey key;
    private byte[] file;
    private Long size;

}
