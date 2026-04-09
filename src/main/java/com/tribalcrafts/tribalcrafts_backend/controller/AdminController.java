package com.tribalcrafts.tribalcrafts_backend.controller;

import com.tribalcrafts.tribalcrafts_backend.entity.Order;
import com.tribalcrafts.tribalcrafts_backend.entity.Product;
import com.tribalcrafts.tribalcrafts_backend.entity.User;
import com.tribalcrafts.tribalcrafts_backend.repository.OrderRepository;
import com.tribalcrafts.tribalcrafts_backend.repository.ProductRepository;
import com.tribalcrafts.tribalcrafts_backend.repository.UserRepository;
import com.tribalcrafts.tribalcrafts_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5174")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private NotificationService notificationService;

    // Dashboard Statistics
    @GetMapping("/users/count")
    public Map<String, Long> getTotalUsers() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", userRepository.count());
        return response;
    }

    @GetMapping("/products/count")
    public Map<String, Long> getTotalProducts() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", productRepository.count());
        return response;
    }

    @GetMapping("/orders/count")
    public Map<String, Long> getTotalOrders() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", orderRepository.count());
        return response;
    }

    @GetMapping("/revenue")
    public Map<String, BigDecimal> getTotalRevenue() {
        Map<String, BigDecimal> response = new HashMap<>();
        BigDecimal revenue = orderRepository.getTotalRevenue();
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }
        response.put("total", revenue);
        return response;
    }

    // Get monthly revenue for analytics
    @GetMapping("/revenue/monthly")
    public Map<String, Object> getMonthlyRevenue() {
        Map<String, Object> response = new HashMap<>();
        List<Object[]> monthlyData = orderRepository.getMonthlyRevenue();
        response.put("data", monthlyData);
        return response;
    }

    // Get recent transactions
    @GetMapping("/transactions/recent")
    public List<Order> getRecentTransactions() {
        return orderRepository.findTop10ByOrderByCreatedAtDesc();
    }

    // Get pending products (for consultant)
    @GetMapping("/products/pending")
    public List<Product> getPendingProducts() {
        return productRepository.findByStatus("PENDING");
    }

    // Get products pending admin approval (after consultant approved)
    @GetMapping("/products/pending-admin")
    public List<Product> getProductsPendingAdmin() {
        return productRepository.findByStatus("CONSULTANT_APPROVED");
    }

    // Admin final approval of product
    @PutMapping("/products/{id}/final-approve")
    public Map<String, String> finalApproveProduct(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStatus("APPROVED");
        product.setAdminNotes(body.get("notes"));
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
        
        // Send approval notification to artisan
        notificationService.addNotification(
            product.getUserId(),
            "success",
            "Product Approved! 🎉",
            "Your product '" + product.getName() + "' has been approved and is now live on the marketplace.",
            "/artisan-dashboard"
        );
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product approved successfully and is now live");
        return response;
    }

    // Admin reject after consultant approval
    @PutMapping("/products/{id}/final-reject")
    public Map<String, String> finalRejectProduct(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStatus("REJECTED");
        product.setAdminNotes(body.get("notes"));
        product.setRejectionReason(body.get("reason"));
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
        
        // Send rejection notification to artisan
        notificationService.addNotification(
            product.getUserId(),
            "alert",
            "Product Rejected by Admin ❌",
            "Your product '" + product.getName() + "' was rejected.\nReason: " + body.get("reason"),
            "/artisan-dashboard"
        );
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product rejected");
        return response;
    }

    @DeleteMapping("/products/{id}/reject")
    public Map<String, String> rejectProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product rejected successfully");
        return response;
    }

    @PutMapping("/users/{id}/status")
    public User updateUserStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(body.get("status"));
        return userRepository.save(user);
    }
    
    // Get all orders with payment details
    @GetMapping("/orders/all")
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }
    
    // Update order payment status
    @PutMapping("/orders/{id}/payment-status")
    public Map<String, String> updatePaymentStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaymentStatus(body.get("paymentStatus"));
        orderRepository.save(order);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Payment status updated");
        return response;
    }
}