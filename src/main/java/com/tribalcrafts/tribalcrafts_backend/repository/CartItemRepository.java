package com.tribalcrafts.tribalcrafts_backend.repository;

import com.tribalcrafts.tribalcrafts_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByUserId(Long userId);
    
    void deleteByUserId(Long userId);
    
    void deleteByIdAndUserId(Long id, Long userId);
    
    // Add this method to check for existing items
    CartItem findByUserIdAndProductId(Long userId, Long productId);
}