package com.shopflow.shpflow.repository;

import com.shopflow.shpflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByRole(User.Role role);  // ← AJOUTER cette ligne
}