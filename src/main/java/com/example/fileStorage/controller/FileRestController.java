package com.example.fileStorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.fileStorage.service.S3StorageService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileRestController {

    private final S3StorageService s3StorageService;

    @Autowired
    public FileRestController(S3StorageService s3StorageService) {
        this.s3StorageService = s3StorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "") String folder) {

        Map<String, String> result = s3StorageService.uploadFile(file, folder);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, String>>> listFiles(
            @RequestParam(value = "folder", defaultValue = "") String folder) {

        List<Map<String, String>> files = s3StorageService.listFiles(folder);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{folder}/{filename:.+}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String folder,
            @PathVariable String filename) {

        String key = folder + "/" + filename;
        byte[] data = s3StorageService.downloadFile(key);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(data);
    }

    @GetMapping("/metadata/{folder}/{filename:.+}")
    public ResponseEntity<Map<String, String>> getFileMetadata(
            @PathVariable String folder,
            @PathVariable String filename) {

        String key = folder + "/" + filename;
        Map<String, String> metadata = s3StorageService.getFileMetadata(key);
        return ResponseEntity.ok(metadata);
    }

    @DeleteMapping("/{folder}/{filename:.+}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable String folder,
            @PathVariable String filename) {

        String key = folder + "/" + filename;
        s3StorageService.deleteFile(key);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/folders")
    public ResponseEntity<List<String>> listFolders() {
        List<String> folders = s3StorageService.listFolders();
        return ResponseEntity.ok(folders);
    }
}