// src/main/java/com/shopflow/shpflow/repository/AddressRepository.java
package com.shopflow.shpflow.repository;
import com.shopflow.shpflow.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);
    Optional<Address> findByUserIdAndPrincipalTrue(Long userId);
}