package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.storage;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.config.minio.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    @Override
    public void upload(String objectKey, InputStream stream, long size, String contentType) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectKey)
                    .stream(stream, size, -1)
                    .contentType(contentType)
                    .build());
            log.info("Object '{}' uploaded to bucket '{}'", objectKey, properties.getBucket());
        } catch (Exception e) {
            log.error("Failed to upload object '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.DOCUMENT_STORAGE_ERROR);
        }
    }

    @Override
    public InputStream download(String objectKey) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            log.error("Failed to download object '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.DOCUMENT_STORAGE_ERROR);
        }
    }

    @Override
    public void remove(String objectKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectKey)
                    .build());
            log.info("Object '{}' removed from bucket '{}'", objectKey, properties.getBucket());
        } catch (Exception e) {
            log.error("Failed to remove object '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.DOCUMENT_STORAGE_ERROR);
        }
    }
}
