package com.shopflow.shpflow.dto.response;
import com.shopflow.shpflow.entity.Review;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Integer note;
    private String commentaire;
    private Boolean approuve;
    private LocalDateTime dateCreation;
    private CustomerInfo customer;
    private ProductInfo product;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CustomerInfo {
        private Long id;
        private String prenom;
        private String nom;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ProductInfo {
        private Long id;
        private String nom;
        private String images;
    }

    public static ReviewResponse fromEntity(Review r) {
        if (r == null) return null;
        CustomerInfo c = r.getCustomer() == null ? null :
            CustomerInfo.builder()
                .id(r.getCustomer().getId())
                .prenom(r.getCustomer().getPrenom())
                .nom(r.getCustomer().getNom()).build();
        ProductInfo p = r.getProduct() == null ? null :
            ProductInfo.builder()
                .id(r.getProduct().getId())
                .nom(r.getProduct().getNom())
                .images(r.getProduct().getImages()).build();
        return ReviewResponse.builder()
            .id(r.getId()).note(r.getNote())
            .commentaire(r.getCommentaire())
            .approuve(r.getApprouve())
            .dateCreation(r.getDateCreation())
            .customer(c).product(p).build();
    }
}