package com.shopflow.shpflow.controller;

import com.shopflow.shpflow.dto.request.LoginRequest;
import com.shopflow.shpflow.dto.request.RegisterRequest;
import com.shopflow.shpflow.dto.response.AuthResponse;
import com.shopflow.shpflow.entity.User;
import com.shopflow.shpflow.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            authService.register(
                req.getPrenom(), req.getNom(), req.getEmail(), req.getPassword(),
                req.getRole() != null ? req.getRole() : User.Role.CUSTOMER,
                req.getNomBoutique(), req.getDescriptionBoutique(), req.getLogoBoutique()
            ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req.getEmail(), req.getPassword()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String header) {
        return ResponseEntity.ok(authService.refreshToken(header.replace("Bearer ", "")));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        String token = authService.requestPasswordReset(body.get("email"));
        return ResponseEntity.ok(Map.of("message", "Lien envoyé", "token", token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        authService.resetPassword(body.get("token"), body.get("newPassword"));
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé"));
    }
}