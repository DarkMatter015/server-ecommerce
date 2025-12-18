package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.publisher.mail;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.dto.mail.EmailEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue emailQueue;
    private final ObjectMapper objectMapper;

    public void send(EmailEventDTO emailEventDTO) {
        try {
            String message = objectMapper.writeValueAsString(emailEventDTO);
            rabbitTemplate.convertAndSend(emailQueue.getName(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error sending EMAIL message to RabbitMQ", e);
        }
    }
}
