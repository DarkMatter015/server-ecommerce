package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.password;

public class InvalidTokenException extends IllegalArgumentException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
