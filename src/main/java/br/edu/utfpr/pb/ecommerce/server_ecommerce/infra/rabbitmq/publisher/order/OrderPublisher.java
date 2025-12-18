package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.publisher.order;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.dto.order.OrderEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue orderQueue;
    private final ObjectMapper objectMapper;

    public void send(OrderEventDTO orderEventDTO) {
        try {
            String message = objectMapper.writeValueAsString(orderEventDTO);
            rabbitTemplate.convertAndSend(orderQueue.getName(), message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error sending ORDER message to RabbitMQ", e);
        }
    }
}
