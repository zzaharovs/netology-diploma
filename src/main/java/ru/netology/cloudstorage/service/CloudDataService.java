package ru.netology.cloudstorage.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.response.FileInfo;

import java.io.IOException;
import java.util.List;

public interface CloudDataService {

    List<FileInfo> getFileNames();

    HttpStatus uploadFile(String fileName, MultipartFile file) throws IOException;

    HttpStatus editFileName(String currentFileName, String newFileName);

    byte [] getFileByName(String fileName);

    HttpStatus deleteFile(String fileName);




}
