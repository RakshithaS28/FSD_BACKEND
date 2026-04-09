package com.tribalcrafts.tribalcrafts_backend.repository;

import com.tribalcrafts.tribalcrafts_backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    // Add this method
    List<OrderItem> findByOrderId(Long orderId);
}