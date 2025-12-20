package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.exception;

import feign.FeignException;

public class ShipmentException extends FeignException {
    public ShipmentException(String message) {
        super(400, message);
    }
}
