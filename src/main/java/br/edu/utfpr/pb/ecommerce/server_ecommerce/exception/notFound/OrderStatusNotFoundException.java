package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

import jakarta.persistence.EntityNotFoundException;

public class OrderStatusNotFoundException extends EntityNotFoundException {
    public OrderStatusNotFoundException(String message) {
        super(message);
    }
}
