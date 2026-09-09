// src/main/java/com/shopflow/shpflow/repository/CartRepository.java
package com.shopflow.shpflow.repository;
import com.shopflow.shpflow.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCustomerId(Long customerId);
}