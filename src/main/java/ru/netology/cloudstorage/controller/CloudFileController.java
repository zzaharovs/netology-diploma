package ru.netology.cloudstorage.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudstorage.entity.response.FileInfo;
import ru.netology.cloudstorage.service.CloudDataService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(
        origins = {"http://localhost:8080"},
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class CloudFileController {

    private final CloudDataService dataService;

    @GetMapping("/list")
    public List<FileInfo> getFilenameList(Integer limit) {
        return dataService.getFileNames();
    }

    @PostMapping("/file")
    public ResponseEntity<HttpStatus> putFile(String filename,
                                              MultipartFile file) throws IOException {
        return new ResponseEntity<>(dataService.uploadFile(filename, file));
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFileByFileName(String filename) {
        return new ResponseEntity<>(dataService.getFileByName(filename), HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<HttpStatus> deleteFileByFileName(String filename) {
        return new ResponseEntity<>(dataService.deleteFile(filename));
    }

    @PutMapping("/file")
    public ResponseEntity<HttpStatus> editFileName(@RequestParam(name = "filename") String currentFileName,
                                                   @RequestBody() Map<String, String> body) {
        final String newFilenameHeader = "filename";
        return new ResponseEntity<>(dataService.editFileName(currentFileName, body.get(newFilenameHeader)));
    }


}
