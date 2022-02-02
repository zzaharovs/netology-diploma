package ru.netology.cloudstorage.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.db.FileInfoEntity;
import ru.netology.cloudstorage.entity.db.FileInfoKey;
import ru.netology.cloudstorage.entity.db.UserEntity;
import ru.netology.cloudstorage.entity.response.FileInfo;
import ru.netology.cloudstorage.repo.CloudFilesRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CloudDataServiceImpl implements CloudDataService {

    private final CloudFilesRepo filesRepo;

    @Override
    public List<FileInfo> getFileNames() {
        String username = "sss";
        return filesRepo.findByUsername(username).stream()
                .map(x -> new FileInfo(x.getFileName(), x.getSize()))
                .collect(Collectors.toList());
    }

    @Override
    public HttpStatus uploadFile(String authToken, String fileName, MultipartFile file) throws IOException {

        String username = "sss";

        FileInfoEntity userFile = FileInfoEntity.builder()
                .fileName(fileName)
                .username(username)
                .file(file.getBytes())
                .size(file.getSize())
                .build();

        filesRepo.saveAndFlush(userFile);
        System.out.printf("upload %s %s%n", authToken, fileName);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus editFileName(String authToken, String currentFileName, String newFileName) {
        String username = "sss";
        filesRepo.editFileName(username, currentFileName, newFileName);
        return HttpStatus.OK;
    }

    @Override
    public byte[] getFileByName(String authToken, String fileName) {

        String username = "sss";
        System.out.printf("get %s %s%n", authToken, fileName);
        return filesRepo.getById(new FileInfoKey(fileName, username)).getFile();
    }

    @Override
    public HttpStatus deleteFile(String authToken, String fileName) {
        String username = "sss";
        System.out.printf("delete %s %s%n", authToken, fileName);
        filesRepo.deleteById(new FileInfoKey(fileName, username));
        return HttpStatus.OK;
    }
}
