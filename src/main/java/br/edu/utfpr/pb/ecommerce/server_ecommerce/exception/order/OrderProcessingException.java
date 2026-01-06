package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.order;

import lombok.Getter;

@Getter
public abstract class OrderProcessingException extends RuntimeException {
    private final String genericKey;
    private final String specificKey;

    public OrderProcessingException(String genericKey, String specificKey) {
        super(genericKey);
        this.genericKey = genericKey;
        this.specificKey = specificKey;
    }
}
