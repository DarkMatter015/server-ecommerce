package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.productStockUpdated;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductStockUpdatedPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final Queue productStockUpdatedQueue;
    private final ObjectMapper objectMapper;

    public void send(ProductStockUpdatedEventDTO productStockUpdatedEventDTO) {
        try {
            String message = objectMapper.writeValueAsString(productStockUpdatedEventDTO);
            rabbitTemplate.convertAndSend(productStockUpdatedQueue.getName(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error sending PRODUCT_STOCK_UPDATED message to RabbitMQ", e);
        }
    }
}
