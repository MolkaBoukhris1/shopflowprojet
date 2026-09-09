// src/main/java/com/shopflow/shpflow/entity/Order.java
package com.shopflow.shpflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "orders")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroCommande;

    @JsonIgnoreProperties({"motDePasse","actif","dateCreation"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address adresseLivraison;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<OrderItem> lignes;

    @Builder.Default private Double sousTotal      = 0.0;
    @Builder.Default private Double fraisLivraison = 5.0;
    @Builder.Default private Double totalTTC       = 0.0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Statut statut = Statut.PENDING;

    private LocalDateTime dateCommande;

    @PrePersist
    protected void onCreate() {
        dateCommande = LocalDateTime.now();
        if (numeroCommande == null)
            numeroCommande = "ORD-" + LocalDateTime.now().getYear()
                    + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }

    public boolean isAnnulable() {
        return statut == Statut.PENDING || statut == Statut.PAID;
    }

    public enum Statut { PENDING, PAID, PROCESSING, SHIPPED, DELIVERED, CANCELLED }
}