package com.tribalcrafts.tribalcrafts_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tribalcrafts.tribalcrafts_backend.entity.Product;
import java.util.List;
import java.util.Map;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByStatus(String status);
    
    List<Product> findByUserId(Long userId);
    
    long countByUserId(Long userId);
    
    @Query(value = "SELECT p.name as name, COUNT(oi.id) as sales FROM products p " +
                   "JOIN order_items oi ON p.id = oi.product_id " +
                   "WHERE p.user_id = :userId " +
                   "GROUP BY p.id ORDER BY sales DESC LIMIT 5", nativeQuery = true)
    List<Map<String, Object>> findTopProductsByArtisan(@Param("userId") Long userId);
}