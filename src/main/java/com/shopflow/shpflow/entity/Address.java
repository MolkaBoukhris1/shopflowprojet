// src/main/java/com/shopflow/shpflow/entity/Address.java
package com.shopflow.shpflow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "addresses")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false) private String rue;
    @Column(nullable = false) private String ville;
    @Column(nullable = false) private String codePostal;
    @Column(nullable = false) private String pays;

    @Builder.Default private Boolean principal = false;
}