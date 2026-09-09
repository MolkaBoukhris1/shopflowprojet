// src/main/java/com/shopflow/shpflow/controller/UserController.java
// NOUVEAU FICHIER — n'existait pas dans votre projet
package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.dto.request.UpdateProfileRequest;
import com.shopflow.shpflow.dto.request.UserRoleRequest;
import com.shopflow.shpflow.dto.response.UserResponse;
import com.shopflow.shpflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ADMIN — liste tous les utilisateurs
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(
            userService.getAll().stream().map(UserResponse::fromEntity).toList());
    }

    // ADMIN — détail
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.getById(id)));
    }

    // ADMIN — activer/désactiver
    @PutMapping("/{id}/toggle-actif")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> toggleActif(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.toggleActif(id)));
    }

    // ADMIN — changer rôle
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleRequest req) {
        return ResponseEntity.ok(
            UserResponse.fromEntity(userService.changeRole(id, req.getRole())));
    }

    // Tous rôles — mon profil
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails u) {
        return ResponseEntity.ok(UserResponse.fromEntity(userService.getByEmail(u.getUsername())));
    }

    // Tous rôles — modifier mon profil
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthenticationPrincipal UserDetails u,
            @Valid @RequestBody UpdateProfileRequest req) {
        return ResponseEntity.ok(
            UserResponse.fromEntity(
                userService.updateProfile(u.getUsername(), req.getPrenom(), req.getNom())));
    }
}
