package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util;

public class IncorrectPasswordException extends IllegalArgumentException {
    public IncorrectPasswordException(String message) {
        super(message);
    }
}
