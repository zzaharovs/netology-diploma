package ru.netology.cloudstorage.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.db.FileInfoEntity;
import ru.netology.cloudstorage.entity.db.FileInfoKey;
import ru.netology.cloudstorage.entity.response.FileInfo;
import ru.netology.cloudstorage.repo.CloudFilesRepo;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CloudDataServiceImpl implements CloudDataService {

    private final CloudFilesRepo filesRepo;

    @Override
    public List<FileInfo> getFileNames() {
        return filesRepo.findByName(getUsernameFromContext());
    }

    @Override
    public HttpStatus uploadFile(String fileName, MultipartFile file) throws IOException {
        FileInfoEntity userFile = FileInfoEntity.builder()
                .fileName(fileName)
                .username(getUsernameFromContext())
                .file(file.getBytes())
                .size(file.getSize())
                .build();
        filesRepo.saveAndFlush(userFile);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus editFileName(String currentFileName, String newFileName) {
        filesRepo.editFileName(getUsernameFromContext(), currentFileName, newFileName);
        return HttpStatus.OK;
    }

    @Override
    public byte[] getFileByName(String fileName) {
        return filesRepo.getById(new FileInfoKey(fileName, getUsernameFromContext())).getFile();
    }

    @Override
    public HttpStatus deleteFile(String fileName) {
        filesRepo.deleteById(new FileInfoKey(fileName, getUsernameFromContext()));
        return HttpStatus.OK;
    }

    private String getUsernameFromContext() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
