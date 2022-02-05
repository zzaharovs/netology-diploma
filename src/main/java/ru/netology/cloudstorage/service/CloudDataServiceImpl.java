package ru.netology.cloudstorage.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.db.FileInfoEntity;
import ru.netology.cloudstorage.entity.db.FileInfoKey;
import ru.netology.cloudstorage.entity.response.FileInfo;
import ru.netology.cloudstorage.repo.CloudFilesRepo;
import ru.netology.cloudstorage.security.CloudUserDetailsService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CloudDataServiceImpl implements CloudDataService {

    private final CloudFilesRepo filesRepo;
    private final CloudUserDetailsService userDetailsService;

    @Override
    public List<FileInfo> getFileNames() {
        log.info("Запрошен список файлов пользователя {}", userDetailsService.getUsernameByContext());
        return filesRepo.findByName(userDetailsService.getUsernameByContext());
    }

    @Override
    public HttpStatus uploadFile(String fileName, MultipartFile file) throws IOException {
        log.info("Попытка загрузки файла {} пользователем {}", fileName, userDetailsService.getUsernameByContext());
        FileInfoEntity userFile = FileInfoEntity.builder()
                .fileName(fileName)
                .username(userDetailsService.getUsernameByContext())
                .file(file.getBytes())
                .size(file.getSize())
                .build();
        filesRepo.saveAndFlush(userFile);
        log.info("Данные загружены");
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus editFileName(String currentFileName, String newFileName) {
        log.info("Попытка изменения имени файла {} пользователем {}", currentFileName, userDetailsService.getUsernameByContext());
        filesRepo.editFileName(userDetailsService.getUsernameByContext(), currentFileName, newFileName);
        log.info("Имя файла успешно изменено на {}", newFileName);
        return HttpStatus.OK;
    }

    @Override
    public byte[] getFileByName(String fileName) {
        log.info("Запрос файла {} пользователем {}", fileName, userDetailsService.getUsernameByContext());
        return filesRepo.getById(new FileInfoKey(fileName, userDetailsService.getUsernameByContext())).getFile();
    }

    @Override
    public HttpStatus deleteFile(String fileName) {
        log.info("Попытка удаления файла {} пользователем {}", fileName, userDetailsService.getUsernameByContext());
        filesRepo.deleteById(new FileInfoKey(fileName, userDetailsService.getUsernameByContext()));
        log.info("Файл успешно удален");
        return HttpStatus.OK;
    }

}
