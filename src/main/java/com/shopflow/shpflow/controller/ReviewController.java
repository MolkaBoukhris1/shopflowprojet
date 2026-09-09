// src/main/java/com/shopflow/shpflow/controller/ReviewController.java
// REMPLACE l'ancien — utilise ReviewRequest, retourne ReviewResponse
package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.dto.request.ReviewRequest;
import com.shopflow.shpflow.dto.response.ReviewResponse;
import com.shopflow.shpflow.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @Valid @RequestBody ReviewRequest req,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ReviewResponse.fromEntity(reviewService.create(
                user.getUsername(), req.getProductId(), req.getNote(), req.getCommentaire())));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(
            reviewService.getByProduct(productId).stream().map(ReviewResponse::fromEntity).toList());
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(
            reviewService.getMyReviews(user.getUsername())
                .stream().map(ReviewResponse::fromEntity).toList());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewResponse>> getPending() {
        return ResponseEntity.ok(
            reviewService.getPending().stream().map(ReviewResponse::fromEntity).toList());
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(ReviewResponse.fromEntity(reviewService.approve(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        reviewService.reject(id);
        return ResponseEntity.noContent().build();
    }
}
