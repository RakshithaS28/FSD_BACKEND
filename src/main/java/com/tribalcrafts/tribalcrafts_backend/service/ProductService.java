package com.tribalcrafts.tribalcrafts_backend.service;

import com.tribalcrafts.tribalcrafts_backend.entity.Product;
import com.tribalcrafts.tribalcrafts_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get only APPROVED products for customers
    public List<Product> getAllApprovedProducts() {
        return productRepository.findByStatus("APPROVED");
    }

    // Get all products (for admin/consultant)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        // Default status is PENDING
        if (product.getStatus() == null) {
            product.setStatus("PENDING");
        }
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByUserId(Long userId) {
        return productRepository.findByUserId(userId);
    }

    // Get single product by ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    // Update product (for edit/resubmit functionality)
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Update fields
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setArtisan(updatedProduct.getArtisan());
        
        // Reset status to PENDING for re-verification
        existingProduct.setStatus("PENDING");
        
        // Clear previous rejection/consultant notes
        existingProduct.setConsultantNotes(null);
        existingProduct.setRejectionReason(null);
        existingProduct.setAdminNotes(null);
        
        // Update timestamp
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(existingProduct);
    }
}