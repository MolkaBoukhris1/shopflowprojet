// src/main/java/com/shopflow/shpflow/entity/Product.java
package com.shopflow.shpflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "products")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Affiche seulement id, prenom, nom du vendeur — pas son motDePasse
    @JsonIgnoreProperties({"motDePasse", "actif", "dateCreation"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double prix;

    private Double prixPromo;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Builder.Default
    private Boolean actif = true;

    @Column(columnDefinition = "TEXT")
    private String images;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<ProductVariant> variantes;

    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() { dateCreation = LocalDateTime.now(); }

    public Double getRemisePourcentage() {
        if (prixPromo != null && prix != null && prix > 0)
            return Math.round(((prix - prixPromo) / prix) * 100.0 * 10) / 10.0;
        return null;
    }
}