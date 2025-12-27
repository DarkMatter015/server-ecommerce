package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;

public class ResourceNotFoundWithException extends BaseException {
    public ResourceNotFoundWithException(Object... args) {
        super(ErrorCode.ENTITY_NOT_FOUND_WITH, args);
    }
}
