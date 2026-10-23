package com.uburakcakmak.order.presentation;
import com.uburakcakmak.order.application.OrderApplicationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.UUID;
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderApplicationService orderApplicationService;
    @PostMapping
    public ResponseEntity<UUID> createOrder(@RequestBody CreateOrderRequest request) {
        UUID orderId = orderApplicationService.createOrder(
                request.getCustomerId(), request.getProductId(), request.getQuantity(), request.getTotalAmount()
        );
        return ResponseEntity.ok(orderId);
    }
    @Data
    static class CreateOrderRequest {
        private UUID customerId;
        private UUID productId;
        private Integer quantity;
        private BigDecimal totalAmount;
    }
}
