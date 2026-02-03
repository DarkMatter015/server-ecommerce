package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.syncProducts;

import java.math.BigDecimal;

public record SyncProductEventDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String category
) {
}
