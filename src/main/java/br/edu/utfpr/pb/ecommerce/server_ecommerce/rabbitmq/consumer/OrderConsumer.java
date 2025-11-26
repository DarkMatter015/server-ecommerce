package br.edu.utfpr.pb.ecommerce.server_ecommerce.rabbitmq.consumer;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.order.OrderEventDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.order.OrderRequestServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("!test")
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final OrderRequestServiceImpl orderRequestService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumer(String message) throws JsonProcessingException {
        log.info("Consuming message: {}", message);
        var orderEventDTO = objectMapper.readValue(message, OrderEventDTO.class);
        orderRequestService.processOrder(orderEventDTO);
    }
}
