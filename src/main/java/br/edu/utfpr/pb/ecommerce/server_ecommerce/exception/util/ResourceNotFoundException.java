package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(Class<?> entity, Object id) {
        super(ErrorCode.ENTITY_NOT_FOUND, entity.getSimpleName(), id);
    }
    public ResourceNotFoundException(ErrorCode code, Object... args) {
        super(code, args);
    }
}
