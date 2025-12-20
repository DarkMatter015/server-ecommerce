package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.brasilAPI.exception;

import jakarta.persistence.EntityNotFoundException;

public class CepException extends EntityNotFoundException {
    public CepException(String message) {
        super(message);
    }
}
