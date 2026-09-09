package com.shopflow.shpflow.dto.response;
import com.shopflow.shpflow.entity.User;
import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private Long id;
    private String prenom;
    private String nom;
    private String email;
    private String role;
    private Boolean actif;
    private LocalDateTime dateCreation;

    public static UserResponse fromEntity(User u) {
        if (u == null) return null;
        return UserResponse.builder()
            .id(u.getId()).prenom(u.getPrenom()).nom(u.getNom())
            .email(u.getEmail()).role(u.getRole().name())
            .actif(u.getActif()).dateCreation(u.getDateCreation()).build();
    }
}