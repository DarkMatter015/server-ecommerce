package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidQuantityException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.util.validation.strategies.Validator;
import org.springframework.stereotype.Component;

@Component
public class OrderStockValidator implements Validator<OrderValidationContext> {

    @Override
    public void validate(OrderValidationContext context) {
        for (OrderItemRequestDTO item : context.getOrderRequest().getOrderItems()) {
            Product product = context.getProducts().get(item.getProductId());
            // Product existence is assumed to be checked before or during map creation
            // but we should be safe.
            if (product != null) {
                if (item.getQuantity() > product.getQuantityAvailableInStock()) {
                    throw new InvalidQuantityException(
                            "Quantity greater than that available in the product stock. Product: " + product.getName() +
                                    ". Quantity available: " + product.getQuantityAvailableInStock()
                    );
                }
            }
        }
    }
}
