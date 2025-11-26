package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
