package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class OrderConsumer {

    private final ProcessOrder processOrder;

    @RabbitListener(queues = "${order.queue.name}")
    public void consumer(OrderEventDTO orderEventDTO) {
        processOrder.processOrder(orderEventDTO);
    }
}
