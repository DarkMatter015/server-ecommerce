package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.notFound;

public class AuthenticatedUserNotFoundException extends RuntimeException {
    public AuthenticatedUserNotFoundException(String message) {
        super(message);
    }
}
