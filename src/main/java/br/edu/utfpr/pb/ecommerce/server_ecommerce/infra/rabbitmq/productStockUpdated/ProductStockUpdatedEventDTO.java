package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.productStockUpdated;

import lombok.Builder;

@Builder
public record ProductStockUpdatedEventDTO(
        Long productId,
        String productName,
        Integer stockQuantity
) {
}
