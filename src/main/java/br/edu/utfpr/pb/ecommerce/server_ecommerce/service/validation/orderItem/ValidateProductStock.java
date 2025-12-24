package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation.orderItem;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderItemDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.validateQuantityOfProduct;

@Component
public class ValidateProductStock implements IValidationOrderItem {

    @Override
    public void validate(List<OrderItemDTO> orderItemDTOS, Map<Long, Product> productMap) {
        for (OrderItemDTO item : orderItemDTOS) {
            Product product = productMap.get(item.getProductId());
            if (product != null)
                validateQuantityOfProduct(item.getQuantity(), product);
        }
    }
}
