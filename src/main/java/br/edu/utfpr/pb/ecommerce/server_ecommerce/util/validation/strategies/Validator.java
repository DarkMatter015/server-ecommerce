package br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.strategies;

public interface Validator<T> {
    void validate(T target);
}
