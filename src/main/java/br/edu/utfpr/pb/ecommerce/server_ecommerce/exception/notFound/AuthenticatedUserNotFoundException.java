package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

import jakarta.persistence.EntityNotFoundException;

public class AuthenticatedUserNotFoundException extends EntityNotFoundException {
    public AuthenticatedUserNotFoundException(String message) {
        super(message);
    }
}
