package com.shopflow.shpflow.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockRequest {
    @NotNull private Integer delta;
}