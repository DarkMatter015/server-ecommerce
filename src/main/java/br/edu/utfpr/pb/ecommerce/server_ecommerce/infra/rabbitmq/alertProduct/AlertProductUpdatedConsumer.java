package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.alertProduct;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.alertProduct.process.ProcessStockAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class AlertProductUpdatedConsumer {

    private final ProcessStockAlert processStockAlert;

    @RabbitListener(queues = "${alertProduct.queue.name}")
    public void consumeAlertProductUpdatedMessage(AlertProductUpdatedEventDTO alertProductUpdatedEventDTO) {
        log.info("Consuming PRODUCT_STOCK_UPDATED message: {}", alertProductUpdatedEventDTO);
        processStockAlert.processStockAlerts(alertProductUpdatedEventDTO);
    }
}
