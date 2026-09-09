package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.dto.request.OrderRequest;
import com.shopflow.shpflow.dto.request.OrderStatusRequest;
import com.shopflow.shpflow.dto.response.OrderResponse;
import com.shopflow.shpflow.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // CUSTOMER — passer commande
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest req,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            OrderResponse.fromEntity(
                orderService.placeOrder(user.getUsername(), req.getAddressId())));
    }

    // CUSTOMER — ses commandes
    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
            orderService.getMyOrders(user.getUsername())
                .stream().map(OrderResponse::fromEntity).toList());
    }

    // SELLER — commandes de ses produits
    @GetMapping("/seller")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<List<OrderResponse>> getSellerOrders(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
            orderService.getSellerOrders(user.getUsername())
                .stream().map(OrderResponse::fromEntity).toList());
    }

    // Détail commande
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(OrderResponse.fromEntity(orderService.getById(id)));
    }

    // ADMIN — toutes les commandes
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(
            orderService.getAll().stream().map(OrderResponse::fromEntity).toList());
    }

    // SELLER/ADMIN — changer statut
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusRequest req) {
        return ResponseEntity.ok(OrderResponse.fromEntity(
            orderService.updateStatut(id, req.getStatut().name())));
    }

    // CUSTOMER — annuler
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
            OrderResponse.fromEntity(orderService.cancel(id, user.getUsername())));
    }
}