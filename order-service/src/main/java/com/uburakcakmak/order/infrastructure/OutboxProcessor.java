package com.uburakcakmak.order.infrastructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {
    private final OutboxMessageRepository outboxMessageRepository;
    private final RabbitTemplate rabbitTemplate;
    @Scheduled(fixedDelay = 5000)
    public void processOutboxMessages() {
        List<OutboxMessage> messages = outboxMessageRepository.findByProcessedFalseOrderByCreatedAtAsc();
        for (OutboxMessage message : messages) {
            try {
                rabbitTemplate.convertAndSend("ecommerce.exchange", "order.created", message.getPayload());
                message.setProcessed(true);
                outboxMessageRepository.save(message);
                log.info("Published outbox message: {}", message.getId());
            } catch (Exception e) {
                log.error("Failed to process outbox message: {}", message.getId(), e);
            }
        }
    }
}
