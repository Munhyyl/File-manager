package com.example.fileStorage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public Map<String, String> uploadFile(MultipartFile file, String folderName) {
        try {
            String key = folderName + "/" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            Map<String, String> response = new HashMap<>();
            response.put("key", key);
            response.put("url", getFileUrl(key));
            response.put("filename", file.getOriginalFilename());
            response.put("contentType", file.getContentType());
            response.put("size", String.valueOf(file.getSize()));

            return response;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public List<Map<String, String>> listFiles(String folderName) {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folderName + "/")
                .build();

        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

        return listObjectsResponse.contents().stream()
                .map(s3Object -> {
                    Map<String, String> fileInfo = new HashMap<>();
                    fileInfo.put("key", s3Object.key());
                    fileInfo.put("url", getFileUrl(s3Object.key()));
                    fileInfo.put("size", String.valueOf(s3Object.size()));
                    fileInfo.put("lastModified", s3Object.lastModified().toString());
                    return fileInfo;
                })
                .collect(Collectors.toList());
    }

    public byte[] downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            return s3Client.getObject(getObjectRequest).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }

    public Map<String, String> getFileMetadata(String key) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("key", key);
        metadata.put("url", getFileUrl(key));
        metadata.put("contentType", headObjectResponse.contentType());
        metadata.put("contentLength", String.valueOf(headObjectResponse.contentLength()));
        metadata.put("lastModified", headObjectResponse.lastModified().toString());

        return metadata;
    }

    public void deleteFile(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public String getFileUrl(String key) {
        return "https://" + bucketName + ".s3." + s3Client.serviceClientConfiguration().region() + ".amazonaws.com/" + key;
    }
    public List<String> listFolders() {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .delimiter("/")
                .build();

        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);

        return listObjectsResponse.commonPrefixes().stream()
                .map(CommonPrefix::prefix)
                .collect(Collectors.toList());
    }
}