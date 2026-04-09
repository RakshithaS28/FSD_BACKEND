package com.tribalcrafts.tribalcrafts_backend.service;

import com.tribalcrafts.tribalcrafts_backend.dto.CartRequest;
import com.tribalcrafts.tribalcrafts_backend.entity.CartItem;
import com.tribalcrafts.tribalcrafts_backend.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem addToCart(CartRequest request) {
        // Check if item already exists in cart
        CartItem existingItem = cartItemRepository.findByUserIdAndProductId(
            request.getUserId(), 
            request.getProductId()
        );
        
        if (existingItem != null) {
            // Update quantity if already exists
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            return cartItemRepository.save(existingItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(request.getUserId());
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setProductName(request.getProductName());
            cartItem.setPrice(request.getPrice());
            cartItem.setImageUrl(request.getImageUrl());
            cartItem.setArtisan(request.getArtisan());
            return cartItemRepository.save(cartItem);
        }
    }

    public List<CartItem> getCartByUser(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public void removeCartItem(Long userId, Long cartItemId) {
        cartItemRepository.deleteByIdAndUserId(cartItemId, userId);
    }
}