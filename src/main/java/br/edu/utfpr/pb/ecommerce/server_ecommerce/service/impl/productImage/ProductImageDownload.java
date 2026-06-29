package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.productImage;

import java.io.InputStream;

public record ProductImageDownload(InputStream stream, String filename, String contentType, long size) {
}
