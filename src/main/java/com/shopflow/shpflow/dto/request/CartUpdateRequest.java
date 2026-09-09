package com.shopflow.shpflow.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CartUpdateRequest {
    @NotNull @Min(0) private Integer quantite;
}