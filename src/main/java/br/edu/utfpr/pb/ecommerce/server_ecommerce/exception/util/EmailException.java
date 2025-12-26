package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;

public class EmailException extends BaseException {
    public EmailException(ErrorCode code, Object... args) {
        super(code, args);
    }
}
