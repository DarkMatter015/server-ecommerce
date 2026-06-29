package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.storage;

import java.io.InputStream;

public interface StorageService {

    void upload(String objectKey, InputStream stream, long size, String contentType);

    InputStream download(String objectKey);

    void remove(String objectKey);

    void upload(String bucket, String objectKey, InputStream stream, long size, String contentType);

    InputStream download(String bucket, String objectKey);

    void remove(String bucket, String objectKey);
}
