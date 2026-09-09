// src/main/java/com/shopflow/shpflow/repository/ReviewRepository.java
// FICHIER COMPLET — REMPLACE L'ANCIEN
package com.shopflow.shpflow.repository;

import com.shopflow.shpflow.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Avis approuvés d'un produit (affichage public)
    List<Review> findByProductIdAndApprouveTrue(Long productId);

    // Tous les avis d'un client
    List<Review> findByCustomerId(Long customerId);

    // Avis en attente de modération (ADMIN)
    List<Review> findByApprouveFalse();

    // Vérifier si un client a déjà laissé un avis pour un produit
    boolean existsByCustomerIdAndProductId(Long customerId, Long productId);

    // Note moyenne d'un produit (avis approuvés seulement)
    @Query("SELECT AVG(r.note) FROM Review r WHERE r.product.id = :productId AND r.approuve = true")
    Double getAveragByProduct(@Param("productId") Long productId);
}
