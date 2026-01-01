package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue orderQueue;

    public void send(OrderEventDTO orderEventDTO) {
        rabbitTemplate.convertAndSend(orderQueue.getName(), orderEventDTO);
    }
}
