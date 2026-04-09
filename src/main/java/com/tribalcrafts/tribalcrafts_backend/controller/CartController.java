package com.tribalcrafts.tribalcrafts_backend.controller;

import com.tribalcrafts.tribalcrafts_backend.dto.CartRequest;
import com.tribalcrafts.tribalcrafts_backend.entity.CartItem;
import com.tribalcrafts.tribalcrafts_backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:5174")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public CartItem addToCart(@RequestBody CartRequest request) {
        return cartService.addToCart(request);
    }

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }

    @DeleteMapping("/clear/{userId}")
    public String clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return "Cart cleared";
    }

    @DeleteMapping("/{userId}/item/{cartItemId}")
    public String removeCartItem(@PathVariable Long userId, @PathVariable Long cartItemId) {
        cartService.removeCartItem(userId, cartItemId);
        return "Item removed";
    }
}