package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
