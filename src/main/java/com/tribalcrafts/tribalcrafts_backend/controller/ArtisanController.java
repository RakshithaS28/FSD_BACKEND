package com.tribalcrafts.tribalcrafts_backend.controller;

import com.tribalcrafts.tribalcrafts_backend.entity.Order;
import com.tribalcrafts.tribalcrafts_backend.repository.OrderRepository;
import com.tribalcrafts.tribalcrafts_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/artisan")
@CrossOrigin(origins = "http://localhost:5174")
public class ArtisanController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/{userId}/products/count")
    public Map<String, Long> getProductsCount(@PathVariable Long userId) {
        long count = productRepository.countByUserId(userId);
        return Map.of("count", count);
    }

    @GetMapping("/{userId}/sales")
    public Map<String, BigDecimal> getTotalSales(@PathVariable Long userId) {
        BigDecimal sales = orderRepository.getTotalSalesByArtisan(userId);
        if (sales == null) sales = BigDecimal.ZERO;
        return Map.of("total", sales);
    }

    @GetMapping("/{userId}/orders/pending")
    public Map<String, Long> getPendingOrders(@PathVariable Long userId) {
        long count = orderRepository.countPendingOrdersByArtisan(userId);
        return Map.of("count", count);
    }

    @GetMapping("/{userId}/orders/recent")
    public List<Map<String, Object>> getRecentOrders(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findRecentOrdersByArtisan(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Limit to 10 most recent orders
        int limit = Math.min(orders.size(), 10);
        for (int i = 0; i < limit; i++) {
            Order order = orders.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", order.getId());
            map.put("product", order.getProductName());  // Now works!
            map.put("customer", order.getCustomerName()); // Now works!
            map.put("status", order.getStatus());
            map.put("totalAmount", order.getTotalAmount());
            map.put("createdAt", order.getCreatedAt());
            result.add(map);
        }
        
        return result;
    }

    @GetMapping("/{userId}/products/top")
    public List<Map<String, Object>> getTopProducts(@PathVariable Long userId) {
        return productRepository.findTopProductsByArtisan(userId);
    }
}