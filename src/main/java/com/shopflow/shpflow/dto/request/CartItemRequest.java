package com.shopflow.shpflow.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotNull private Long productId;
    @NotNull @Min(1) private Integer quantite;
    private Long variantId;
}