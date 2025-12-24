package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.order;

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
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final ProcessOrder processOrder;

    @RabbitListener(queues = "${order.queue.name}")
    public void consumer(String message) throws JsonProcessingException {
        log.info("Consuming ORDER message: {}", message);
        var orderEventDTO = objectMapper.readValue(message, OrderEventDTO.class);
        processOrder.processOrder(orderEventDTO);
    }
}
