package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.ErrorCode;

public class InvalidShipmentException extends OrderProcessingException {
    public InvalidShipmentException(ErrorCode errorCode) {
        super("order.status.message.cancelled.shipment", errorCode.getMessageKey());
    }
}
