package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.password;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException(ErrorCode code, Object... args) {
        super(code, args);
    }
}
