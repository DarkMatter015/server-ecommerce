package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderItemDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;

import java.util.List;
import java.util.Map;

public interface IValidationOrderItem {
    void validate(List<OrderItemDTO> orderItemDTOS, Map<Long, Product> productMap);
}
