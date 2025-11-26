package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
