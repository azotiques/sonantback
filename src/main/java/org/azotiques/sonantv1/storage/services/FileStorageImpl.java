package org.azotiques.sonantv1.storage.services;

import lombok.RequiredArgsConstructor;
import org.azotiques.sonantv1.storage.FileStorageApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageImpl implements FileStorageApi {

    private final S3Client s3Client;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Value("${application.storage.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File is too large");
            }

            String key = UUID.randomUUID().toString();

            s3Client.putObject(
              PutObjectRequest.builder()
                      .bucket(bucket)
                      .key(key)
                      .contentType(file.getContentType())
                      .build(),
                RequestBody.fromInputStream(
                        file.getInputStream(),
                        file.getSize()
                )
            );

            return key;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file");
        }
    }

    @Override
    public String getUrl(String key) {
        return s3Client.utilities()
                .getUrl(GetUrlRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build())
                .toExternalForm();
    }

    @Override
    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build()
        );
    }
}
