package br.edu.utfpr.pb.ecommerce.server_ecommerce.infra.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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

    //    PRODUCT_STOCK_UPDATED
    @Value("${productStockUpdated.queue.name}")
    private String productStockUpdatedQueueName;

    @Value("${productStockUpdated.dlx.name}")
    private String productStockUpdatedDlxName;

    @Value("${productStockUpdated.dlq.name}")
    private String productStockUpdatedDlqName;

    @Value("${productStockUpdated.dlk.key}")
    private String productStockUpdatedDlrkKey;

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

    //    PRODUCT_STOCK_UPDATED
    @Bean
    public Queue productStockUpdatedDlq() {
        return QueueBuilder.durable(productStockUpdatedDlqName).build();
    }

    @Bean
    public DirectExchange productStockUpdatedDlx() {
        return new DirectExchange(productStockUpdatedDlxName);
    }

    @Bean
    public Binding bindproductStockUpdatedDlq() {
        return BindingBuilder.bind(productStockUpdatedDlq()).to(productStockUpdatedDlx()).with(productStockUpdatedDlrkKey);
    }

    @Bean
    public Queue productStockUpdatedQueue() {
        return QueueBuilder.durable(productStockUpdatedQueueName)
                .withArgument("x-dead-letter-exchange", productStockUpdatedDlxName)
                .withArgument("x-dead-letter-routing-key", productStockUpdatedDlrkKey)
                .build();
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
