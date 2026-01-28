package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.syncProducts;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SyncProductPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${products.exchange.name}")
    private String productsExchangeName;

    @Value("${products.routing.created}")
    private String productCreatedRoutingKey;

    @Value("${products.routing.updated}")
    private String productUpdatedRoutingKey;

    public void sendSyncCreated(SyncProductEventDTO syncProductEventDTO) {
        rabbitTemplate.convertAndSend(productsExchangeName, productCreatedRoutingKey, syncProductEventDTO);
    }

    public void sendSyncUpdated(SyncProductEventDTO syncProductEventDTO) {
        rabbitTemplate.convertAndSend(productsExchangeName, productUpdatedRoutingKey, syncProductEventDTO);
    }
}
