package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.config.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties properties;

    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();

        ensureBucketExists(client);
        return client;
    }

    private void ensureBucketExists(MinioClient client) {
        try {
            boolean exists = client.bucketExists(
                    BucketExistsArgs.builder().bucket(properties.getBucket()).build());
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(properties.getBucket()).build());
                log.info("MinIO bucket '{}' created successfully", properties.getBucket());
            } else {
                log.info("MinIO bucket '{}' already exists", properties.getBucket());
            }
        } catch (Exception e) {
            log.error("Failed to initialize MinIO bucket '{}': {}", properties.getBucket(), e.getMessage());
        }
    }
}
