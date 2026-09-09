// src/main/java/com/shopflow/shpflow/repository/SellerProfileRepository.java
package com.shopflow.shpflow.repository;
import com.shopflow.shpflow.entity.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SellerProfileRepository extends JpaRepository<SellerProfile, Long> {
    Optional<SellerProfile> findByUserId(Long userId);
}