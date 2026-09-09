package com.shopflow.shpflow.repository;

import com.shopflow.shpflow.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN p.categories c WHERE p.actif = true AND " +
           "(:search IS NULL OR p.nom LIKE %:search% OR " +
           "p.description LIKE %:search%) AND " +
           "(:categorieId IS NULL OR c.id = :categorieId) AND " +
           "(:prixMin IS NULL OR p.prix >= :prixMin) AND " +
           "(:prixMax IS NULL OR p.prix <= :prixMax)")
    Page<Product> searchProducts(
        @Param("search") String search,
        @Param("categorieId") Long categorieId,
        @Param("prixMin") Double prixMin,
        @Param("prixMax") Double prixMax,
        Pageable pageable);

    List<Product> findBySellerId(Long sellerId);

    @Query("SELECT p FROM Product p WHERE p.actif = true AND p.prixPromo IS NOT NULL")
    List<Product> findPromoProducts();

    @Query("SELECT p FROM Product p LEFT JOIN OrderItem oi ON oi.product = p " +
           "WHERE p.actif = true GROUP BY p ORDER BY COUNT(oi) DESC")
    List<Product> findTopSelling(Pageable pageable);

    @Query("SELECT p.nom, SUM(oi.quantite) FROM OrderItem oi JOIN oi.product p " +
           "GROUP BY p.nom ORDER BY SUM(oi.quantite) DESC")
    List<Object[]> getTopProducts(Pageable pageable);
}