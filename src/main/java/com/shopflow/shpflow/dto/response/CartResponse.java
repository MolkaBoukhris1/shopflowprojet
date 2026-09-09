package com.shopflow.shpflow.dto.response;

import com.shopflow.shpflow.entity.Cart;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private Long id;
    private Object customer;
    private Object lignes;
    private String couponCode;
    private Double remise;

    public static CartResponse fromEntity(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .customer(cart.getCustomer())
                .lignes(cart.getLignes())
                .couponCode(cart.getCouponCode())
                .remise(cart.getRemise())
                .build();
    }
}