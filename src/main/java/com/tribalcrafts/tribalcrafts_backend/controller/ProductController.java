package com.tribalcrafts.tribalcrafts_backend.controller;

import com.tribalcrafts.tribalcrafts_backend.entity.Product;
import com.tribalcrafts.tribalcrafts_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all APPROVED products for customers
    @GetMapping
    public List<Product> getAllApprovedProducts() {
        return productService.getAllApprovedProducts();
    }

    // Get all products (for admin/consultant)
    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get single product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }

    @GetMapping("/user/{userId}")
    public List<Product> getProductsByUser(@PathVariable Long userId) {
        return productService.getProductsByUserId(userId);
    }
}