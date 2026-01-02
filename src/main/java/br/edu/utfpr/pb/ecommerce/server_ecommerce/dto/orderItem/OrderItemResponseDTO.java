package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.orderItem;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.product.ProductResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {

    private Long id;

    private Long orderId;

    private ProductResponseDTO product;

    private BigDecimal totalPrice;

    private Integer quantity;
}
