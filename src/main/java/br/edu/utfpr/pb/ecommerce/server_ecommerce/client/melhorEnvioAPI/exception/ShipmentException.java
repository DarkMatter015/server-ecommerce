package br.edu.utfpr.pb.ecommerce.server_ecommerce.client.melhorEnvioAPI.exception;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;

public class ShipmentException extends BaseException {
    public ShipmentException(ErrorCode code, Object... args) {
        super(code, args);
    }
}
