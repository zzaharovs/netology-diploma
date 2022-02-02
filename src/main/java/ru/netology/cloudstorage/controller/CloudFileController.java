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
        origins = "http://localhost:8080/",
        allowCredentials = "true",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}
)
public class CloudFileController {

    private final CloudDataService dataService;

    @GetMapping("/list")
    public List<FileInfo> getFilenameList(@RequestHeader("auth-token") String authToken, Integer limit) {
        return dataService.getFileNames();
    }

    @PostMapping("/file")
    public ResponseEntity<HttpStatus> putFile(@RequestHeader("auth-token") String authToken, String filename,
                                              MultipartFile file) throws IOException {
        return new ResponseEntity<>(dataService.uploadFile(authToken, filename, file));
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFileByFileName(@RequestHeader("auth-token") String authToken, String filename) {
        return new ResponseEntity<>(dataService.getFileByName(authToken, filename), HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<HttpStatus> deleteFileByFileName(@RequestHeader("auth-token") String authToken, String filename) {
        return new ResponseEntity<>(dataService.deleteFile(authToken, filename));
    }

    @PutMapping("/file")
    public ResponseEntity<HttpStatus> editFileName(@RequestHeader("auth-token") String authToken,
                                                   @RequestParam(name = "filename") String currentFileName,
                                                   @RequestBody() Map<String, String> body) {
        final String filenameHeader = "filename";
        return new ResponseEntity<>(dataService.editFileName(authToken, currentFileName, body.get(filenameHeader)));
    }


}
