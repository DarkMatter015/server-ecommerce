package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class OrderConsumer {

    private final ProcessOrder processOrder;

    @RabbitListener(queues = "${order.queue.name}")
    public void consumer(OrderEventDTO orderEventDTO) {
        log.info("Consuming ORDER message: {}", orderEventDTO);
        processOrder.processOrder(orderEventDTO);
    }
}
