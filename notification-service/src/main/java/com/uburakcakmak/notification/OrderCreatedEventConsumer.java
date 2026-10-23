package com.uburakcakmak.notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uburakcakmak.shared.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventConsumer {
    private final ObjectMapper objectMapper;
    @RabbitListener(queues = "notification.queue")
    public void handleOrderCreatedEvent(String message) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
            log.info("Notification Service received OrderCreatedEvent. Sending email to Customer ID: {} for Order: {}", event.getCustomerId(), event.getOrderId());
        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }
}
