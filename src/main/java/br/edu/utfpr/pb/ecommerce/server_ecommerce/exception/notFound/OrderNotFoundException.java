package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

import jakarta.persistence.EntityNotFoundException;

public class OrderNotFoundException extends EntityNotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
