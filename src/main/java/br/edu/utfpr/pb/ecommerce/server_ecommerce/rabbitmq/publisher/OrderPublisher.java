package br.edu.utfpr.pb.ecommerce.server_ecommerce.rabbitmq.publisher;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderEventDTO;
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
    private final Queue queue;
    private final ObjectMapper objectMapper;

    public void send(OrderEventDTO orderEventDTO) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(orderEventDTO);
        rabbitTemplate.convertAndSend(queue.getName(), message);
    }
}
