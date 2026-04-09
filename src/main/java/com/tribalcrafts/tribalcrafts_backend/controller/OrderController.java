package com.tribalcrafts.tribalcrafts_backend.controller;

import com.tribalcrafts.tribalcrafts_backend.dto.OrderRequest;
import com.tribalcrafts.tribalcrafts_backend.entity.Order;
import com.tribalcrafts.tribalcrafts_backend.repository.OrderRepository;
import com.tribalcrafts.tribalcrafts_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        try {
            Order order = orderService.placeOrder(request);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }
    
    // Update order status (for admin dashboard)
    @PutMapping("/{orderId}/status")
    public Map<String, String> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> body) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(body.get("status"));
        orderRepository.save(order);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order status updated successfully");
        response.put("orderId", String.valueOf(orderId));
        response.put("newStatus", body.get("status"));
        return response;
    }
    
    // Update payment status (for admin dashboard)
    @PutMapping("/{orderId}/payment-status")
    public Map<String, String> updatePaymentStatus(@PathVariable Long orderId, @RequestBody Map<String, String> body) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaymentStatus(body.get("paymentStatus"));
        orderRepository.save(order);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Payment status updated successfully");
        response.put("orderId", String.valueOf(orderId));
        response.put("newPaymentStatus", body.get("paymentStatus"));
        return response;
    }
    
    // Get all orders (for admin dashboard)
    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }
    
    // Get order by ID
    @GetMapping("/details/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
}