package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.productStockUpdated;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class ProductStockUpdatedConsumer {

    private final ObjectMapper objectMapper;
    private final StockAlertOrchestrator stockAlertOrchestrator;

    @RabbitListener(queues = "${productStockUpdated.queue.name}")
    public void consumeProductStockUpdatedMessage(String message) throws JsonProcessingException {
        log.info("Consuming PRODUCT_STOCK_UPDATED message: {}", message);
        var productStockEventDTO = objectMapper.readValue(message, ProductStockUpdatedEventDTO.class);
        stockAlertOrchestrator.processStockAlerts(productStockEventDTO);
    }
}
