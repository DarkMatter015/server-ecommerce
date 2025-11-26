package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
