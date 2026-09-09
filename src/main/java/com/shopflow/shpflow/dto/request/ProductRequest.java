package com.shopflow.shpflow.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Set;

@Data
public class ProductRequest {
    @NotBlank private String nom;
    private String description;
    @NotNull @Positive private Double prix;
    @Positive private Double prixPromo;
    @NotNull @Min(0) private Integer stock;
    private String images;
    @NotEmpty private Set<Long> categoryIds;
}