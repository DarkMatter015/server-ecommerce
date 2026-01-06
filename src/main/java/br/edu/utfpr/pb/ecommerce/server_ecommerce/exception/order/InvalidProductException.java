package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;

public class InvalidProductException extends OrderProcessingException {
    public InvalidProductException(ErrorCode errorCode) {
        super("order.status.message.cancelled.product", errorCode.getMessageKey());
    }
}
