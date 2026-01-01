package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.alertProduct;

import lombok.Builder;

@Builder
public record AlertProductUpdatedEventDTO(
        Long productId,
        String productName,
        Integer stockQuantity
) {
}
