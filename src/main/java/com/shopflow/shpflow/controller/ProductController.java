package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.dto.request.ProductRequest;
import com.shopflow.shpflow.dto.request.StockRequest;
import com.shopflow.shpflow.dto.response.PageResponse;
import com.shopflow.shpflow.dto.response.ProductResponse;
import com.shopflow.shpflow.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categorieId,
            @RequestParam(required = false) Double prixMin,
            @RequestParam(required = false) Double prixMax,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<ProductResponse> result = productService
                .getAll(search, categorieId, prixMin, prixMax, page, size);

        return ResponseEntity.ok(PageResponse.of(result));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductResponse>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<ProductResponse> result = productService
                .getAll(q, null, null, null, page, size);

        return ResponseEntity.ok(PageResponse.of(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<ProductResponse>> getTopSelling() {
        return ResponseEntity.ok(productService.getTopSelling());
    }

    @GetMapping("/promo")
    public ResponseEntity<List<ProductResponse>> getPromo() {
        return ResponseEntity.ok(productService.getPromo());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<List<ProductResponse>> getMyProducts(
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                productService.getMyProducts(user.getUsername()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody ProductRequest req,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                productService.create(
                        req.getNom(),
                        req.getDescription(),
                        req.getPrix(),
                        req.getPrixPromo(),
                        req.getStock(),
                        req.getImages(),
                        req.getCategoryIds(),
                        user.getUsername()
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest req,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                productService.update(
                        id,
                        req.getNom(),
                        req.getDescription(),
                        req.getPrix(),
                        req.getPrixPromo(),
                        req.getStock(),
                        req.getImages(),
                        req.getCategoryIds(),
                        user.getUsername()
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        productService.softDelete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<ProductResponse> toggleActif(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                productService.toggleActif(id, user.getUsername()));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    public ResponseEntity<ProductResponse> adjustStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequest req,
            @AuthenticationPrincipal UserDetails user) {

        return ResponseEntity.ok(
                productService.adjustStock(id, req.getDelta(), user.getUsername()));
    }
}