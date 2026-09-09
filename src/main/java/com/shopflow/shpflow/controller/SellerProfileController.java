package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.entity.SellerProfile;
import com.shopflow.shpflow.repository.SellerProfileRepository;
import com.shopflow.shpflow.repository.UserRepository;
import com.shopflow.shpflow.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/seller/profile")
@RequiredArgsConstructor
public class SellerProfileController {

    private final SellerProfileRepository sellerRepo;
    private final UserRepository userRepo;

    @GetMapping
    public ResponseEntity<SellerProfile> getMyProfile(
            @AuthenticationPrincipal UserDetails u) {
        User seller = userRepo.findByEmail(u.getUsername())
            .orElseThrow(() -> new RuntimeException("Vendeur non trouvé"));
        SellerProfile profile = sellerRepo.findByUserId(seller.getId())
            .orElseThrow(() -> new RuntimeException("Profil boutique non trouvé"));
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<SellerProfile> updateProfile(
            @AuthenticationPrincipal UserDetails u,
            @RequestBody Map<String, String> body) {
        User seller = userRepo.findByEmail(u.getUsername())
            .orElseThrow(() -> new RuntimeException("Vendeur non trouvé"));
        SellerProfile profile = sellerRepo.findByUserId(seller.getId())
            .orElseThrow(() -> new RuntimeException("Profil boutique non trouvé"));
        if (body.get("nomBoutique") != null)
            profile.setNomBoutique(body.get("nomBoutique"));
        if (body.get("description") != null)
            profile.setDescription(body.get("description"));
        if (body.get("logo") != null)
            profile.setLogo(body.get("logo"));
        return ResponseEntity.ok(sellerRepo.save(profile));
    }
}