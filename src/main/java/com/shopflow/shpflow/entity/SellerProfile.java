// src/main/java/com/shopflow/shpflow/entity/SellerProfile.java
package com.shopflow.shpflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "seller_profiles")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class SellerProfile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"motDePasse","actif","dateCreation"})
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false) private String nomBoutique;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(columnDefinition = "TEXT")
    private String logo;

    @Builder.Default private Double note = 0.0;
}
