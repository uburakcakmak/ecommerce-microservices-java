package com.uburakcakmak.order.infrastructure;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "ecommerce.exchange";
    public static final String ROUTING_KEY = "order.created";
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    @Bean public TopicExchange exchange() { return new TopicExchange(EXCHANGE_NAME); }
    @Bean public Queue inventoryQueue() { return new Queue(INVENTORY_QUEUE, true); }
    @Bean public Queue paymentQueue() { return new Queue(PAYMENT_QUEUE, true); }
    @Bean public Queue notificationQueue() { return new Queue(NOTIFICATION_QUEUE, true); }
    @Bean public Binding inventoryBinding(Queue inventoryQueue, TopicExchange exchange) {
        return BindingBuilder.bind(inventoryQueue).to(exchange).with(ROUTING_KEY);
    }
    @Bean public Binding paymentBinding(Queue paymentQueue, TopicExchange exchange) {
        return BindingBuilder.bind(paymentQueue).to(exchange).with(ROUTING_KEY);
    }
    @Bean public Binding notificationBinding(Queue notificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notificationQueue).to(exchange).with(ROUTING_KEY);
    }
}
