package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception;

public class EmailException extends RuntimeException {
    public EmailException(String message, Exception e) {
        super(message, e);
    }
}
