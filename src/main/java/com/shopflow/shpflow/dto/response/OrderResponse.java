package com.shopflow.shpflow.dto.response;
import com.shopflow.shpflow.entity.Order;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String numeroCommande;
    private String statut;
    private Boolean annulable;
    private Double sousTotal;
    private Double fraisLivraison;
    private Double totalTTC;
    private LocalDateTime dateCommande;
    private CustomerInfo customer;
    private AddressInfo adresseLivraison;
    private List<OrderItemInfo> lignes;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CustomerInfo {
        private Long id;
        private String prenom;
        private String nom;
        private String email;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AddressInfo {
        private Long id;
        private String rue;
        private String ville;
        private String codePostal;
        private String pays;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class OrderItemInfo {
        private Long id;
        private String nomProduit;
        private String imagesProduit;
        private Integer quantite;
        private Double prixUnitaire;
        private Double sousTotal;
        private String variantAttribut;
        private String variantValeur;
    }

    public static OrderResponse fromEntity(Order o) {
        if (o == null) return null;
        CustomerInfo c = o.getCustomer() == null ? null :
            CustomerInfo.builder()
                .id(o.getCustomer().getId())
                .prenom(o.getCustomer().getPrenom())
                .nom(o.getCustomer().getNom())
                .email(o.getCustomer().getEmail()).build();
        AddressInfo a = o.getAdresseLivraison() == null ? null :
            AddressInfo.builder()
                .id(o.getAdresseLivraison().getId())
                .rue(o.getAdresseLivraison().getRue())
                .ville(o.getAdresseLivraison().getVille())
                .codePostal(o.getAdresseLivraison().getCodePostal())
                .pays(o.getAdresseLivraison().getPays()).build();
        List<OrderItemInfo> items = o.getLignes() == null ? List.of() :
            o.getLignes().stream().map(oi -> OrderItemInfo.builder()
                .id(oi.getId())
                .nomProduit(oi.getProduct().getNom())
                .imagesProduit(oi.getProduct().getImages())
                .quantite(oi.getQuantite())
                .prixUnitaire(oi.getPrixUnitaire())
                .sousTotal(oi.getPrixUnitaire() * oi.getQuantite())
                .variantAttribut(oi.getVariant() != null ? oi.getVariant().getAttribut() : null)
                .variantValeur(oi.getVariant() != null ? oi.getVariant().getValeur() : null)
                .build()).collect(Collectors.toList());
        return OrderResponse.builder()
            .id(o.getId()).numeroCommande(o.getNumeroCommande())
            .statut(o.getStatut().name()).annulable(o.isAnnulable())
            .sousTotal(o.getSousTotal()).fraisLivraison(o.getFraisLivraison())
            .totalTTC(o.getTotalTTC()).dateCommande(o.getDateCommande())
            .customer(c).adresseLivraison(a).lignes(items).build();
    }
}