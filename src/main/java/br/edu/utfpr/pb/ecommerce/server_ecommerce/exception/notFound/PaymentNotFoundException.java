package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

import jakarta.persistence.EntityNotFoundException;

public class PaymentNotFoundException extends EntityNotFoundException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
