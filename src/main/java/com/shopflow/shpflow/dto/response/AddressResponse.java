package com.shopflow.shpflow.dto.response;
import com.shopflow.shpflow.entity.Address;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
    private Boolean principal;

    public static AddressResponse fromEntity(Address a) {
        if (a == null) return null;
        return AddressResponse.builder()
            .id(a.getId()).rue(a.getRue()).ville(a.getVille())
            .codePostal(a.getCodePostal()).pays(a.getPays())
            .principal(a.getPrincipal()).build();
    }
}