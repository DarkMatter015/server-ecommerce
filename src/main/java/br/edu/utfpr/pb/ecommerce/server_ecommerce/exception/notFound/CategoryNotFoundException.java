package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
