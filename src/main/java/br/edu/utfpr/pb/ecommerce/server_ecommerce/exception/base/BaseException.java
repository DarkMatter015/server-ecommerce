package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorCode code;
    private final Object[] args;

    protected BaseException(ErrorCode code, Object... args) {
        super(code.getMessageKey());
        this.code = code;
        this.args = args;
    }
}
