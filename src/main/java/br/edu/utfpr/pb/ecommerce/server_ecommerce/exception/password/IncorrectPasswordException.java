package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.password;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;

public class IncorrectPasswordException extends BaseException {
    public IncorrectPasswordException(ErrorCode code, Object... args) {
        super(code, args);
    }
}
