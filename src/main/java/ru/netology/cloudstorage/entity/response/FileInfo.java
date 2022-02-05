package ru.netology.cloudstorage.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileInfo {

    private String filename;
    private Long size;

}
