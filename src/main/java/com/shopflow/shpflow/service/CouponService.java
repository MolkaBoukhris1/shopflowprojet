// src/main/java/com/shopflow/shpflow/service/CouponService.java
package com.shopflow.shpflow.service;

import com.shopflow.shpflow.entity.Coupon;
import com.shopflow.shpflow.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepo;

    public List<Coupon> getAll() { return couponRepo.findAll(); }

    public Coupon validate(String code) {
        Coupon c = couponRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Code promo invalide"));
        if (!c.isValide()) throw new RuntimeException("Code promo expiré ou épuisé");
        return c;
    }

    @Transactional
    public Coupon create(String code, Coupon.Type type, Double valeur,
                         LocalDate expiration, Integer usagesMax) {
        return couponRepo.save(Coupon.builder()
                .code(code.toUpperCase()).type(type).valeur(valeur)
                .dateExpiration(expiration)
                .usagesMax(usagesMax != null ? usagesMax : 100).build());
    }

    @Transactional
    public void delete(Long id) { couponRepo.deleteById(id); }
}