// src/main/java/com/shopflow/shpflow/controller/CouponController.java
package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.entity.Coupon;
import com.shopflow.shpflow.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Coupon>> getAll() {
        return ResponseEntity.ok(couponService.getAll());
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<Coupon> validate(@PathVariable String code) {
        return ResponseEntity.ok(couponService.validate(code));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Coupon> create(@RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            couponService.create(
                (String) body.get("code"),
                Coupon.Type.valueOf((String) body.get("type")),
                Double.valueOf(body.get("valeur").toString()),
                body.get("dateExpiration") != null
                    ? LocalDate.parse((String) body.get("dateExpiration")) : null,
                body.get("usagesMax") != null
                    ? Integer.valueOf(body.get("usagesMax").toString()) : null
            ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }
}