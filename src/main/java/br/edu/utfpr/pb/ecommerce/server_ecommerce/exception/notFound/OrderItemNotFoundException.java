package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

import jakarta.persistence.EntityNotFoundException;

public class OrderItemNotFoundException extends EntityNotFoundException {
    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
