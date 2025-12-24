package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation.orderItem;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderItemDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.BusinessException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ValidateProductStatusOrderItem implements IValidationOrderItem {

    @Override
    public void validate(List<OrderItemDTO> orderItemDTOS, Map<Long, Product> productMap) {
        for (OrderItemDTO item : orderItemDTOS) {
            Product product = productMap.get(item.getProductId());
            if (!product.isActive()) throw new BusinessException("Product with id " + product.getId() + " is not active.");
        }
    }
}
