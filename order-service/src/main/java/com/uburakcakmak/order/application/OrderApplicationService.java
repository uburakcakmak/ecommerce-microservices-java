package com.uburakcakmak.order.application;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uburakcakmak.order.domain.Order;
import com.uburakcakmak.order.infrastructure.OrderRepository;
import com.uburakcakmak.order.infrastructure.OutboxMessage;
import com.uburakcakmak.order.infrastructure.OutboxMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class OrderApplicationService {
    private final OrderRepository orderRepository;
    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;
    @Transactional
    public UUID createOrder(UUID customerId, UUID productId, Integer quantity, BigDecimal totalAmount) {
        Order order = new Order(customerId, productId, quantity, totalAmount);
        orderRepository.save(order);
        order.getDomainEvents().forEach(event -> {
            try {
                OutboxMessage message = OutboxMessage.builder()
                        .id(UUID.randomUUID())
                        .aggregateType("Order")
                        .aggregateId(order.getId().toString())
                        .eventType(event.getClass().getSimpleName())
                        .payload(objectMapper.writeValueAsString(event))
                        .createdAt(LocalDateTime.now())
                        .processed(false)
                        .build();
                outboxMessageRepository.save(message);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Could not serialize event", e);
            }
        });
        order.clearDomainEvents();
        return order.getId();
    }
}
