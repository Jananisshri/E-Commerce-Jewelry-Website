package com.tanishq.repository;

import com.tanishq.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Fetch all products by category
    List<Product> findByCategoryIgnoreCase(String category);

    // Fetch featured products for homepage
    List<Product> findByIsFeaturedTrue();

    // Search products by name
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);

    // Fetch all distinct categories
    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
    List<String> findAllCategories();

    // Fetch by category - max 8 products
    @Query(value = "SELECT * FROM products WHERE LOWER(category) = LOWER(:category) LIMIT 8",
           nativeQuery = true)
    List<Product> findTop8ByCategory(@Param("category") String category);
}