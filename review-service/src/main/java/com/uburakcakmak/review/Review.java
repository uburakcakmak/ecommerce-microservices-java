package com.uburakcakmak.review;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private UUID id;
    private UUID productId;
    private UUID customerId;
    private int rating;
    private String comment;
}
