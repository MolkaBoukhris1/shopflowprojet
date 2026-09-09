// src/main/java/com/shopflow/shpflow/service/UserService.java
// NOUVEAU FICHIER — n'existait pas dans votre projet
package com.shopflow.shpflow.service;

import com.shopflow.shpflow.entity.User;
import com.shopflow.shpflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User getById(Long id) {
        return userRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + id));
    }

    public User getByEmail(String email) {
        return userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));
    }

    @Transactional
    public User toggleActif(Long id) {
        User user = getById(id);
        user.setActif(!user.getActif());
        return userRepo.save(user);
    }

    @Transactional
    public User changeRole(Long id, User.Role newRole) {
        User user = getById(id);
        user.setRole(newRole);
        return userRepo.save(user);
    }

    @Transactional
    public User updateProfile(String email, String prenom, String nom) {
        User user = getByEmail(email);
        if (prenom != null) user.setPrenom(prenom);
        if (nom != null) user.setNom(nom);
        return userRepo.save(user);
    }
}
