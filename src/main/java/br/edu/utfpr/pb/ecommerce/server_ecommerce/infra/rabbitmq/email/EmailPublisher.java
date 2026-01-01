package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.email;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue emailQueue;

    public void send(EmailEventDTO emailEventDTO) {
        rabbitTemplate.convertAndSend(emailQueue.getName(), emailEventDTO);
    }
}
