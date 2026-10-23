package com.uburakcakmak.shared.event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderCreatedEvent {
    private UUID orderId;
    private UUID customerId;
    private BigDecimal totalAmount;
    private UUID productId;
    private Integer quantity;
}
