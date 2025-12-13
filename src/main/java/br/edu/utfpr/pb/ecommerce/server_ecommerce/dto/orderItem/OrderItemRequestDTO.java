package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequestDTO {

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    @NotNull
    @Positive(message = "{field.quantity.min}")
    private Integer quantity;
}
