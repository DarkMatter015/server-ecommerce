package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

import jakarta.persistence.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
