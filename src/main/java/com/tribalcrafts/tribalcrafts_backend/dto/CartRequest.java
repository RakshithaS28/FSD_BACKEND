package com.tribalcrafts.tribalcrafts_backend.dto;

public class CartRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double price;
    private String productName;
    private String artisan;
    private String imageUrl;

    // Constructors
    public CartRequest() {}

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getArtisan() { return artisan; }
    public void setArtisan(String artisan) { this.artisan = artisan; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}