package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQConfig {

    //    ORDER
    @Value("${order.queue.name}")
    private String orderQueueName;

    @Value("${order.dlx.name}")
    private String orderDlxName;

    @Value("${order.dlq.name}")
    private String orderDlqName;

    @Value("${order.dlk.key}")
    private String orderDlrkKey;

    //    EMAIL
    @Value("${email.queue.name}")
    private String emailQueueName;

    @Value("${email.dlx.name}")
    private String emailDlxName;

    @Value("${email.dlq.name}")
    private String emailDlqName;

    @Value("${email.dlk.key}")
    private String emailDlrkKey;

    @Value("${email.wait.queue}")
    private String emailWaitQueueName;

    //    PRODUCT_STOCK_UPDATED
    @Value("${alertProduct.queue.name}")
    private String alertProductQueueName;

    @Value("${alertProduct.dlx.name}")
    private String alertProductDlxName;

    @Value("${alertProduct.dlq.name}")
    private String alertProductDlqName;

    @Value("${alertProduct.dlk.key}")
    private String alertProductDlrkKey;

    @Value("${email.wait.time}")
    private int waitTime;

    //    ORDER
    @Bean
    public Queue orderDlq() {
        return QueueBuilder.durable(orderDlqName).build();
    }

    @Bean
    public DirectExchange orderDlx() {
        return new DirectExchange(orderDlxName);
    }

    @Bean
    public Binding bindOrderDlq() {
        return BindingBuilder.bind(orderDlq()).to(orderDlx()).with(orderDlrkKey);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(orderQueueName)
                .withArgument("x-dead-letter-exchange", orderDlxName)
                .withArgument("x-dead-letter-routing-key", orderDlrkKey)
                .build();
    }

    //    EMAIL
    @Bean
    public Queue emailDlq() {
        return QueueBuilder.durable(emailDlqName).build();
    }

    @Bean
    public DirectExchange emailDlx() {
        return new DirectExchange(emailDlxName);
    }

    @Bean
    public Binding bindEmailDlq() {
        return BindingBuilder.bind(emailDlq()).to(emailDlx()).with(emailDlrkKey);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(emailQueueName)
                .withArgument("x-dead-letter-exchange", emailDlxName)
                .withArgument("x-dead-letter-routing-key", emailDlrkKey)
                .build();
    }

    @Bean
    public Queue emailWaitQueue() {
        log.info("Creating waiting queue with TTL of: {} ms", waitTime);
        return QueueBuilder.durable(emailWaitQueueName)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", emailQueueName)
                .withArgument("x-message-ttl", waitTime)
                .build();
    }

    //    alertProduct
    @Bean
    public Queue alertProductDlq() {
        return QueueBuilder.durable(alertProductDlqName).build();
    }

    @Bean
    public DirectExchange alertProductDlx() {
        return new DirectExchange(alertProductDlxName);
    }

    @Bean
    public Binding bindAlertProductDlq() {
        return BindingBuilder.bind(alertProductDlq()).to(alertProductDlx()).with(alertProductDlrkKey);
    }

    @Bean
    public Queue alertProductQueue() {
        return QueueBuilder.durable(alertProductQueueName)
                .withArgument("x-dead-letter-exchange", alertProductDlxName)
                .withArgument("x-dead-letter-routing-key", alertProductDlrkKey)
                .build();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
