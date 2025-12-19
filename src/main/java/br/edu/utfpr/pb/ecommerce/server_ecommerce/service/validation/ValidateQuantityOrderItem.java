package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderItemDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.ValidationUtils.validateQuantityOfProduct;

@RequiredArgsConstructor
public class ValidateQuantityOrderItem implements IValidationOrderItem {

    @Override
    public void validate(List<OrderItemDTO> orderItemDTOS, Map<Long, Product> productMap) {
        for (OrderItemDTO item : orderItemDTOS) {
            Product product = productMap.get(item.getProductId());
            validateQuantityOfProduct(item.getQuantity(), product);
        }
    }
}
