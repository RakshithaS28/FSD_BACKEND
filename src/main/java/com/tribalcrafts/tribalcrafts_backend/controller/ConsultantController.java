package com.tribalcrafts.tribalcrafts_backend.controller;

import com.tribalcrafts.tribalcrafts_backend.entity.Product;
import com.tribalcrafts.tribalcrafts_backend.repository.ProductRepository;
import com.tribalcrafts.tribalcrafts_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/consultant")
@CrossOrigin(origins = "http://localhost:5174")
public class ConsultantController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/products/pending")
    public List<Product> getPendingProducts() {
        return productRepository.findByStatus("PENDING");
    }

    @GetMapping("/products/pending-admin")
    public List<Product> getProductsPendingAdmin() {
        return productRepository.findByStatus("CONSULTANT_APPROVED");
    }

    @GetMapping("/products/rejected")
    public List<Product> getRejectedProducts() {
        return productRepository.findByStatus("REJECTED");
    }

    @PutMapping("/products/{id}/approve")
    public Map<String, String> consultantApprove(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStatus("CONSULTANT_APPROVED");
            product.setConsultantNotes(body.get("notes"));
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
            
            // Send notification to artisan
            notificationService.addNotification(
                product.getUserId(),
                "product",
                "Product Consultant Approved ✅",
                "Your product '" + product.getName() + "' has been approved by consultant. Waiting for admin approval.",
                "/artisan-dashboard"
            );
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product approved by consultant. Waiting for admin approval.");
            response.put("status", product.getStatus());
            return response;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Product not found");
            return response;
        }
    }

    @PutMapping("/products/{id}/reject")
    public Map<String, String> consultantReject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStatus("REJECTED");
            product.setConsultantNotes(body.get("notes"));
            product.setRejectionReason(body.get("reason"));
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
            
            // Send rejection notification to artisan
            String reason = body.get("reason");
            String notes = body.get("notes");
            notificationService.addNotification(
                product.getUserId(),
                "alert",
                "Product Verification Failed ❌",
                "Your product '" + product.getName() + "' was rejected.\nReason: " + reason + "\nDetails: " + notes,
                "/artisan-dashboard"
            );
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product rejected. Artisan has been notified.");
            response.put("status", product.getStatus());
            return response;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Product not found");
            return response;
        }
    }

    @PutMapping("/products/{id}/request-changes")
    public Map<String, String> requestChanges(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStatus("CHANGES_REQUESTED");
            product.setConsultantNotes(body.get("notes"));
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
            
            System.out.println("=== Sending Notification to Artisan ===");
            System.out.println("Artisan User ID: " + product.getUserId());
            System.out.println("Product Name: " + product.getName());
            System.out.println("Notes: " + body.get("notes"));
            
            // Send change request notification to artisan
            notificationService.addNotification(
                product.getUserId(),
                "alert",
                "Changes Requested for Product 🔄",
                "Your product '" + product.getName() + "' needs changes.\n\nFeedback from Consultant:\n" + body.get("notes"),
                "/artisan-dashboard"
            );
            
            System.out.println("Notification sent successfully");
            System.out.println("=====================================");
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Changes requested for product. Artisan has been notified.");
            response.put("status", product.getStatus());
            response.put("notes", body.get("notes"));
            return response;
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Product not found");
            return response;
        }
    }

    @GetMapping("/techniques")
    public List<Map<String, Object>> getDocumentedTechniques() {
        List<Map<String, Object>> techniques = new ArrayList<>();
        
        Map<String, Object> tech1 = new HashMap<>();
        tech1.put("name", "Warli Art");
        tech1.put("completion", 85);
        techniques.add(tech1);
        
        Map<String, Object> tech2 = new HashMap<>();
        tech2.put("name", "Madhubani Painting");
        tech2.put("completion", 92);
        techniques.add(tech2);
        
        Map<String, Object> tech3 = new HashMap<>();
        tech3.put("name", "Bamboo Craft");
        tech3.put("completion", 78);
        techniques.add(tech3);
        
        return techniques;
    }

    @GetMapping("/research")
    public List<Map<String, Object>> getResearchProjects() {
        List<Map<String, Object>> projects = new ArrayList<>();
        
        Map<String, Object> proj1 = new HashMap<>();
        proj1.put("name", "Tribal Pottery Documentation");
        proj1.put("progress", 65);
        projects.add(proj1);
        
        Map<String, Object> proj2 = new HashMap<>();
        proj2.put("name", "Natural Dye Research");
        proj2.put("progress", 40);
        projects.add(proj2);
        
        Map<String, Object> proj3 = new HashMap<>();
        proj3.put("name", "Weaving Traditions");
        proj3.put("progress", 80);
        projects.add(proj3);
        
        return projects;
    }
}