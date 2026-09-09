package com.shopflow.shpflow.repository;

import com.shopflow.shpflow.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdOrderByDateCommandeDesc(Long customerId);

    List<Order> findTop5ByOrderByDateCommandeDesc();

    @Query("SELECT o.statut, COUNT(o) FROM Order o GROUP BY o.statut")
    List<Object[]> countByStatut();

    @Query("SELECT COALESCE(SUM(o.totalTTC), 0) FROM Order o WHERE o.statut = 'DELIVERED'")
    Double getTotalRevenue();

    @Query("SELECT DISTINCT o FROM Order o " +
           "JOIN o.lignes oi " +
           "JOIN oi.product p " +
           "WHERE p.seller.id = :sellerId " +
           "ORDER BY o.dateCommande DESC")
    List<Order> findOrdersBySeller(@Param("sellerId") Long sellerId);
}

