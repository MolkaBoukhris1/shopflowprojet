package com.shopflow.shpflow.dto.response;

import com.shopflow.shpflow.entity.Product;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String nom;
    private String description;
    private Double prix;
    private Double prixPromo;
    private Double remisePourcentage;
    private Integer stock;
    private Boolean actif;
    private String images;
    private LocalDateTime dateCreation;
    private SellerInfo seller;
    private List<CategoryInfo> categories;
    private List<VariantInfo> variantes;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class SellerInfo {
        private Long id;
        private String prenom;
        private String nom;
        private String nomBoutique;
        private String logoBoutique;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CategoryInfo {
        private Long id;
        private String nom;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class VariantInfo {
        private Long id;
        private String attribut;
        private String valeur;
        private Integer stockSupplementaire;
        private Double prixDelta;
    }

    public static ProductResponse fromEntity(Product p) {
        if (p == null) return null;

        String nomBoutique = null;
        String logoBoutique = null;
        try {
            if (p.getSeller() != null && p.getSeller().getSellerProfile() != null) {
                nomBoutique  = p.getSeller().getSellerProfile().getNomBoutique();
                logoBoutique = p.getSeller().getSellerProfile().getLogo();
            }
        } catch (Exception e) {
            // SellerProfile pas chargé
        }

        SellerInfo s = p.getSeller() == null ? null :
            SellerInfo.builder()
                .id(p.getSeller().getId())
                .prenom(p.getSeller().getPrenom())
                .nom(p.getSeller().getNom())
                .nomBoutique(nomBoutique)
                .logoBoutique(logoBoutique)
                .build();

        List<CategoryInfo> cats = p.getCategories() == null ? List.of() :
            p.getCategories().stream()
                .map(c -> CategoryInfo.builder()
                    .id(c.getId()).nom(c.getNom()).build())
                .collect(Collectors.toList());

        List<VariantInfo> vars = p.getVariantes() == null ? List.of() :
            p.getVariantes().stream()
                .map(v -> VariantInfo.builder()
                    .id(v.getId()).attribut(v.getAttribut())
                    .valeur(v.getValeur())
                    .stockSupplementaire(v.getStockSupplementaire())
                    .prixDelta(v.getPrixDelta()).build())
                .collect(Collectors.toList());

        return ProductResponse.builder()
            .id(p.getId()).nom(p.getNom()).description(p.getDescription())
            .prix(p.getPrix()).prixPromo(p.getPrixPromo())
            .remisePourcentage(p.getRemisePourcentage())
            .stock(p.getStock()).actif(p.getActif()).images(p.getImages())
            .dateCreation(p.getDateCreation())
            .seller(s).categories(cats).variantes(vars).build();
    }
}