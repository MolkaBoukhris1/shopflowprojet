package com.shopflow.shpflow.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CouponRequest {
    @NotBlank private String code;
}