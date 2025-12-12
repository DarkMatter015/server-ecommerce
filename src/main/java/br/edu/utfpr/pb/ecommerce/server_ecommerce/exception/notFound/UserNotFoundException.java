package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
