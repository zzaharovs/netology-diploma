package ru.netology.cloudstorage.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.netology.cloudstorage.entity.db.FileInfoEntity;

@Data
@AllArgsConstructor
public class FileInfo {

    private String filename;
    private Long size;

}
