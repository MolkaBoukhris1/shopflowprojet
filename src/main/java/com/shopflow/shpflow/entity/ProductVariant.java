// src/main/java/com/shopflow/shpflow/entity/ProductVariant.java
package com.shopflow.shpflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "product_variants")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ProductVariant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false) private String attribut;
    @Column(nullable = false) private String valeur;

    @Builder.Default private Integer stockSupplementaire = 0;
    @Builder.Default private Double prixDelta = 0.0;
}