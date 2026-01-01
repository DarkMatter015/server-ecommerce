package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.alertProduct;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertProductUpdatedPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final Queue alertProductQueue;

    public void send(AlertProductUpdatedEventDTO alertProductUpdatedEventDTO) {
        rabbitTemplate.convertAndSend(alertProductQueue.getName(), alertProductUpdatedEventDTO);
    }
}
