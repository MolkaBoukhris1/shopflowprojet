// src/main/java/com/shopflow/shpflow/controller/CartController.java
// REMPLACE l'ancien — utilise CartItemRequest, CartUpdateRequest, CouponRequest
package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.dto.request.CartItemRequest;
import com.shopflow.shpflow.dto.request.CartUpdateRequest;
import com.shopflow.shpflow.dto.request.CouponRequest;
import com.shopflow.shpflow.dto.response.CartResponse;
import com.shopflow.shpflow.entity.Cart;
import com.shopflow.shpflow.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(cartService.getCart(user.getUsername()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @Valid @RequestBody CartItemRequest req,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                cartService.addItem(user.getUsername(), req.getProductId(), req.getQuantite()));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody CartUpdateRequest req,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                cartService.updateItem(user.getUsername(), itemId, req.getQuantite()));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                cartService.removeItem(user.getUsername(), itemId));
    }

    @PostMapping("/coupon")
    public ResponseEntity<CartResponse> applyCoupon(
            @Valid @RequestBody CouponRequest req,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                cartService.applyCoupon(user.getUsername(), req.getCode()));
    }
}