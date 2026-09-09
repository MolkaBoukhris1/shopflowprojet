// src/main/java/com/shopflow/shpflow/entity/Review.java
package com.shopflow.shpflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "reviews")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"motDePasse","actif","dateCreation"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;

    @JsonIgnoreProperties({"seller","categories","variantes","description"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false) private Integer note;
    @Column(columnDefinition = "TEXT") private String commentaire;

    @Builder.Default private Boolean approuve = false;
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() { dateCreation = LocalDateTime.now(); }
}
 