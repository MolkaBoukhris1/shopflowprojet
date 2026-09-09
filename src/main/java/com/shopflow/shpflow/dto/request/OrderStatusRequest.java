package com.shopflow.shpflow.dto.request;
import com.shopflow.shpflow.entity.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusRequest {
    @NotNull private Order.Statut statut;
}