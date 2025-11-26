package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

public class OrderStatusNotFoundException extends RuntimeException {
    public OrderStatusNotFoundException(String message) {
        super(message);
    }
}
