package com.shopflow.shpflow.repository;

import com.shopflow.shpflow.entity.Category;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Modifying
    @Query(value = "DELETE FROM product_categories WHERE category_id = :categoryId",
           nativeQuery = true)
    void deleteProductCategoryLinks(@Param("categoryId") Long categoryId);

    @Modifying
    @Query(value = "UPDATE categories SET parent_id = NULL WHERE parent_id = :id",
           nativeQuery = true)
    void detachChildren(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM categories WHERE id = :id",
           nativeQuery = true)
    void deleteCategory(@Param("id") Long id);
}