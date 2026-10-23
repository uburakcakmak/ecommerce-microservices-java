package com.uburakcakmak.inventory;
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
    @RabbitListener(queues = "inventory.queue")
    public void handleOrderCreatedEvent(String message) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
            log.info("Inventory Service received OrderCreatedEvent for Product ID: {} to deduct {} items.", event.getProductId(), event.getQuantity());
        } catch (Exception e) {
            log.error("Error processing message", e);
        }
    }
}
