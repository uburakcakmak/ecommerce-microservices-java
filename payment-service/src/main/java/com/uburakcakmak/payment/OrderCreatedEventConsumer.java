package com.uburakcakmak.payment;
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
    @RabbitListener(queues = "payment.queue")
    public void handleOrderCreatedEvent(String message) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
            log.info("Payment Service received OrderCreatedEvent for Order ID: {} and amount {}", event.getOrderId(), event.getTotalAmount());
        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }
}
