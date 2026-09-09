package com.shopflow.shpflow.dto.request;

import com.shopflow.shpflow.entity.User;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String prenom;
    @NotBlank private String nom;
    @NotBlank @Email private String email;
    @NotBlank @Size(min = 8) private String password;
    private User.Role role;
    private String nomBoutique;
    private String descriptionBoutique;
    private String logoBoutique;
}