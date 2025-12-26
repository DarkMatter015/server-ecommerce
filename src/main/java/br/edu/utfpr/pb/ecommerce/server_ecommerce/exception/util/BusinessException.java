package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;

public class BusinessException extends BaseException {
    public BusinessException(ErrorCode code, Object... args) {
        super(code, args);
    }
}
