package com.shopflow.shpflow.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank private String prenom;
    @NotBlank private String nom;
}