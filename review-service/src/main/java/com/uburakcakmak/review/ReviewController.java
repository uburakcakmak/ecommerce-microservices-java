package com.uburakcakmak.review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        log.info("Creating a review for product: {} by customer: {}", review.getProductId(), review.getCustomerId());
        review.setId(UUID.randomUUID());
        return ResponseEntity.ok(reviewRepository.save(review));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        log.info("Fetching all reviews");
        return ResponseEntity.ok(reviewRepository.findAll());
    }
}
