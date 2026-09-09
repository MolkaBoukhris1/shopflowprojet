package com.shopflow.shpflow.service;

import com.shopflow.shpflow.dto.response.AuthResponse;
import com.shopflow.shpflow.entity.*;
import com.shopflow.shpflow.repository.*;
import com.shopflow.shpflow.security.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final SellerProfileRepository sellerRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final UserDetailsServiceImpl userDetailsService;

    private final Map<String, String> resetTokens = new ConcurrentHashMap<>();

    @Transactional
    public AuthResponse register(String prenom, String nom, String email, String password,
                                 User.Role role, String nomBoutique,
                                 String descriptionBoutique, String logoBoutique) {
        if (userRepo.existsByEmail(email))
            throw new RuntimeException("Email déjà utilisé");

        User user = User.builder()
                .prenom(prenom).nom(nom).email(email)
                .motDePasse(passwordEncoder.encode(password))
                .role(role != null ? role : User.Role.CUSTOMER)
                .build();
        userRepo.save(user);

        if (user.getRole() == User.Role.SELLER && nomBoutique != null) {
            sellerRepo.save(SellerProfile.builder()
                    .user(user)
                    .nomBoutique(nomBoutique)
                    .description(descriptionBoutique)
                    .logo(logoBoutique)
                    .build());
        }

        log.info("Nouveau compte créé : {} ({})", email, user.getRole());
        return buildAuthResponse(user);
    }

    public AuthResponse login(String email, String password) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (!user.getActif())
            throw new RuntimeException("Compte désactivé. Contactez l'administrateur.");
        return buildAuthResponse(user);
    }

    public AuthResponse refreshToken(String token) {
        String email = jwtUtil.extractEmail(token);
        UserDetails ud = userDetailsService.loadUserByUsername(email);
        if (!jwtUtil.isTokenValid(token, ud))
            throw new RuntimeException("Token invalide ou expiré");
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return buildAuthResponse(user);
    }

    @Transactional
    public String requestPasswordReset(String email) {
        userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aucun compte avec cet email"));
        String token = UUID.randomUUID().toString();
        resetTokens.put(token, email);
        log.info("Token reset généré pour {} : {}", email, token);
        return token;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        String email = resetTokens.get(token);
        if (email == null)
            throw new RuntimeException("Token de réinitialisation invalide ou expiré");
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setMotDePasse(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        resetTokens.remove(token);
        log.info("Mot de passe réinitialisé pour {}", email);
    }

    private AuthResponse buildAuthResponse(User user) {
        UserDetails ud = userDetailsService.loadUserByUsername(user.getEmail());
        return AuthResponse.of(
            jwtUtil.generateAccessToken(ud),
            jwtUtil.generateRefreshToken(ud),
            user
        );
    }
}