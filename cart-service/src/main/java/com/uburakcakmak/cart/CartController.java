package com.uburakcakmak.cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartRepository cartRepository;

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        log.info("Creating a new cart for customerId: {}", cart.getCustomerId());
        cart.setId(UUID.randomUUID());
        return ResponseEntity.ok(cartRepository.save(cart));
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        log.info("Fetching all carts");
        return ResponseEntity.ok(cartRepository.findAll());
    }
}
