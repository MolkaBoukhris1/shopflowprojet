// src/main/java/com/shopflow/shpflow/entity/Coupon.java
package com.shopflow.shpflow.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "coupons")
@Data @NoArgsConstructor @AllArgsConstructor @Builder

public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) private String code;

    @Enumerated(EnumType.STRING) private Type type;

    @Column(nullable = false) private Double valeur;
    private LocalDate dateExpiration;

    @Builder.Default private Integer usagesMax     = 100;
    @Builder.Default private Integer usagesActuels = 0;
    @Builder.Default private Boolean actif         = true;

    public enum Type { PERCENT, FIXED }

    public boolean isValide() {
        return actif && usagesActuels < usagesMax
            && (dateExpiration == null || !LocalDate.now().isAfter(dateExpiration));
    }

    public double calculerRemise(double montant) {
        if (!isValide()) return 0;
        return type == Type.PERCENT ? montant * valeur / 100 : Math.min(valeur, montant);
    }
}