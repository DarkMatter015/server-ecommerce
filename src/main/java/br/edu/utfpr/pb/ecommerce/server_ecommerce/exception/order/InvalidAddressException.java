package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;

public class InvalidAddressException extends OrderProcessingException {
    public InvalidAddressException(ErrorCode errorCode) {
        super("order.status.message.cancelled.address", errorCode.getMessageKey());
    }
}
