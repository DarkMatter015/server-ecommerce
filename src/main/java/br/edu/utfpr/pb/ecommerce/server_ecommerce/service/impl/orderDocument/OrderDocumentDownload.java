package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.orderDocument;

import java.io.InputStream;

public record OrderDocumentDownload(InputStream stream, String filename, String contentType, long size) {
}
