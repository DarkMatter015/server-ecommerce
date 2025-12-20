package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;

public interface IValidationOrder {
    void validate(OrderRequestDTO orderRequestDTO);
}
