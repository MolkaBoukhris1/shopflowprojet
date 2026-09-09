// src/main/java/com/shopflow/shpflow/entity/Cart.java
package com.shopflow.shpflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "carts")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"motDePasse","actif","dateCreation"})
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", unique = true)
    private User customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<CartItem> lignes = new ArrayList<>();

    private String couponCode;
    private Double remise;

    @Builder.Default
    private LocalDateTime dateModification = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() { dateModification = LocalDateTime.now(); }
}