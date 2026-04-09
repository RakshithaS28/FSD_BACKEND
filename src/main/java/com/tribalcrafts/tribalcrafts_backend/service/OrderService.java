package com.tribalcrafts.tribalcrafts_backend.service;

import com.tribalcrafts.tribalcrafts_backend.dto.OrderRequest;
import com.tribalcrafts.tribalcrafts_backend.entity.CartItem;
import com.tribalcrafts.tribalcrafts_backend.entity.Order;
import com.tribalcrafts.tribalcrafts_backend.entity.OrderItem;
import com.tribalcrafts.tribalcrafts_backend.entity.Product;
import com.tribalcrafts.tribalcrafts_backend.repository.CartItemRepository;
import com.tribalcrafts.tribalcrafts_backend.repository.OrderItemRepository;
import com.tribalcrafts.tribalcrafts_backend.repository.OrderRepository;
import com.tribalcrafts.tribalcrafts_backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order placeOrder(OrderRequest request) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(request.getUserId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = 0;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            total += product.getPrice() * cartItem.getQuantity();
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setAddress(request.getAddress());
        order.setTotalAmount(total);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(savedOrder.getId());
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteByUserId(request.getUserId());

        return savedOrder;
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
}