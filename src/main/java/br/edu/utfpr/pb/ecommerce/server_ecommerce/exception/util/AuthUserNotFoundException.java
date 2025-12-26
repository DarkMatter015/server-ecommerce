package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;

public class AuthUserNotFoundException extends BaseException {
    public AuthUserNotFoundException(ErrorCode code, Object... args) {
        super(code, args);
    }
}
