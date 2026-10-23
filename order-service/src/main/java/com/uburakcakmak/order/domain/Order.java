package com.uburakcakmak.order.domain;
import com.uburakcakmak.shared.domain.AggregateRoot;
import com.uburakcakmak.shared.event.OrderCreatedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends AggregateRoot<UUID> {
    @Id
    private UUID id;
    private UUID customerId;
    private UUID productId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private String status;
    public Order(UUID customerId, UUID productId, Integer quantity, BigDecimal totalAmount) {
        this.id = UUID.randomUUID();
        super.setId(this.id);
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.orderDate = LocalDateTime.now();
        this.status = "CREATED";
        registerEvent(new OrderCreatedEvent(this.id, this.customerId, this.totalAmount, this.productId, this.quantity));
    }
}
