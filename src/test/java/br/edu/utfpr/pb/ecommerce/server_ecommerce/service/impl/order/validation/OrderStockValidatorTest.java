package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.validation;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem.OrderItemRequestDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.util.InvalidQuantityException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderStockValidatorTest {

    private final OrderStockValidator validator = new OrderStockValidator();

    @Test
    @DisplayName("Should validate successfully when stock is sufficient")
    void validateSuccess() {
        // Arrange
        Long productId = 1L;
        Product product = Product.builder()
                .name("Guitar")
                .quantityAvailableInStock(10)
                .build();
        product.setId(productId);

        OrderItemRequestDTO itemDTO = OrderItemRequestDTO.builder()
                .productId(productId)
                .quantity(5)
                .build();

        OrderRequestDTO request = OrderRequestDTO.builder()
                .orderItems(List.of(itemDTO))
                .build();

        OrderValidationContext context = OrderValidationContext.builder()
                .orderRequest(request)
                .products(Map.of(productId, product))
                .build();

        // Act & Assert
        assertDoesNotThrow(() -> validator.validate(context));
    }

    @Test
    @DisplayName("Should throw exception when stock is insufficient")
    void validateInsufficientStock() {
        // Arrange
        Long productId = 1L;
        Product product = Product.builder()
                .name("Guitar")
                .quantityAvailableInStock(5)
                .build();
        product.setId(productId);

        OrderItemRequestDTO itemDTO = OrderItemRequestDTO.builder()
                .productId(productId)
                .quantity(10)
                .build();

        OrderRequestDTO request = OrderRequestDTO.builder()
                .orderItems(List.of(itemDTO))
                .build();

        OrderValidationContext context = OrderValidationContext.builder()
                .orderRequest(request)
                .products(Map.of(productId, product))
                .build();

        // Act & Assert
        assertThrows(InvalidQuantityException.class, () -> validator.validate(context));
    }
}
