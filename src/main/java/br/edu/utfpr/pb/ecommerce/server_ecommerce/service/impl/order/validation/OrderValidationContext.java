package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OrderValidationContext {
    private final OrderRequestDTO orderRequest;
    private final Map<Long, Product> products;
}
