package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

import jakarta.persistence.EntityNotFoundException;

public class AddressNotFoundException extends EntityNotFoundException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}
