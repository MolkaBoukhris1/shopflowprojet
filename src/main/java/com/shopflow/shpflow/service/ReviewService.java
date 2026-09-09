// src/main/java/com/shopflow/shpflow/service/ReviewService.java
// FICHIER COMPLET — REMPLACE L'ANCIEN
package com.shopflow.shpflow.service;

import com.shopflow.shpflow.entity.*;
import com.shopflow.shpflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    @Transactional
    public Review create(String email, Long productId, Integer note, String commentaire) {

        // Vérification : note entre 1 et 5
        if (note < 1 || note > 5)
            throw new RuntimeException("La note doit être entre 1 et 5");

        User customer = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Vérification : le client a-t-il acheté ce produit (commande DELIVERED) ?
        boolean aAchete = orderRepo.findByCustomerIdOrderByDateCommandeDesc(customer.getId())
                .stream()
                .filter(o -> o.getStatut() == Order.Statut.DELIVERED)
                .flatMap(o -> o.getLignes().stream())
                .anyMatch(oi -> oi.getProduct().getId().equals(productId));

        if (!aAchete)
            throw new RuntimeException(
                "Vous ne pouvez laisser un avis que sur un produit que vous avez acheté et reçu");

        // Vérification : a-t-il déjà laissé un avis pour ce produit ?
        boolean dejaAvis = reviewRepo.existsByCustomerIdAndProductId(customer.getId(), productId);
        if (dejaAvis)
            throw new RuntimeException("Vous avez déjà laissé un avis pour ce produit");

        Review review = Review.builder()
                .customer(customer)
                .product(product)
                .note(note)
                .commentaire(commentaire)
                .approuve(false) // en attente de modération admin
                .build();

        Review saved = reviewRepo.save(review);

        // Recalcul de la note moyenne du produit
        updateMoyenneProduct(productId);

        return saved;
    }

    // ADMIN — approuver un avis
    @Transactional
    public Review approve(Long id) {
        Review review = reviewRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Avis non trouvé"));
        review.setApprouve(true);
        return reviewRepo.save(review);
    }

    // ADMIN — rejeter (supprimer) un avis
    @Transactional
    public void reject(Long id) {
        reviewRepo.deleteById(id);
    }

    // Avis approuvés d'un produit (public)
    public List<Review> getByProduct(Long productId) {
        return reviewRepo.findByProductIdAndApprouveTrue(productId);
    }

    // Tous les avis en attente (ADMIN)
    public List<Review> getPending() {
        return reviewRepo.findByApprouveFalse();
    }

    // Avis d'un client (CUSTOMER)
    public List<Review> getMyReviews(String email) {
        User customer = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return reviewRepo.findByCustomerId(customer.getId());
    }

    // Recalcul note moyenne automatique du produit
    private void updateMoyenneProduct(Long productId) {
        List<Review> avis = reviewRepo.findByProductIdAndApprouveTrue(productId);
        if (!avis.isEmpty()) {
            
            // La note moyenne peut être stockée dans Product si vous ajoutez le champ
            // Sinon elle est calculée dynamiquement — voir ReviewRepository.getAverage()
        }
    }
}
