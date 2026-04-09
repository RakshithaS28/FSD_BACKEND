package com.tribalcrafts.tribalcrafts_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tribalcrafts.tribalcrafts_backend.entity.Order;
import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o")
    BigDecimal getTotalRevenue();
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.artisanId = :userId")
    BigDecimal getTotalSalesByArtisan(@Param("userId") Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.artisanId = :userId AND o.status = 'PENDING'")
    Long countPendingOrdersByArtisan(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.artisanId = :userId ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByArtisan(@Param("userId") Long userId);
    
    List<Order> findByUserId(Long userId);
    
    long count();
   
    // For Admin Dashboard
    List<Order> findTop10ByOrderByCreatedAtDesc();
    
    List<Order> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT FUNCTION('MONTH', o.createdAt) as month, SUM(o.totalAmount) as revenue FROM Order o GROUP BY FUNCTION('MONTH', o.createdAt)")
    List<Object[]> getMonthlyRevenue();
}