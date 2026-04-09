package com.tribalcrafts.tribalcrafts_backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5174")
public class ChatController {

 
	@Value("${app.gemini.api-key}")
	private String geminiApiKey;
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent?key=";

    @PostMapping("/send")
    public Map<String, String> sendMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        
        Map<String, String> response = new HashMap<>();
        
        try {
            String aiReply = getAIResponse(userMessage);
            response.put("reply", aiReply);
        } catch (Exception e) {
            System.err.println("AI Error: " + e.getMessage());
            response.put("reply", getSmartFallbackResponse(userMessage));
        }
        
        response.put("timestamp", new Date().toString());
        return response;
    }
    
    @SuppressWarnings("unchecked")
    private String getAIResponse(String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            // Build request body for Gemini API
            Map<String, Object> requestBody = new LinkedHashMap<>();
            Map<String, Object> content = new LinkedHashMap<>();
            Map<String, String> part = new LinkedHashMap<>();
            part.put("text", "You are a helpful assistant for Tribal Crafts Marketplace. " +
                           "Answer questions about tribal handicrafts, products, artisans, " +
                           "shipping, returns, payments, and cultural heritage. " +
                           "Keep answers friendly and concise. User asked: " + userMessage);
            
            List<Map<String, String>> parts = Collections.singletonList(part);
            content.put("parts", parts);
            
            List<Map<String, Object>> contents = Collections.singletonList(content);
            requestBody.put("contents", contents);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            String url = GEMINI_URL + geminiApiKey;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, 
                HttpMethod.POST, 
                entity, 
                (Class<Map<String, Object>>)(Class<?>)Map.class
            );
            
            if (response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> candidate = candidates.get(0);
                    Map<String, Object> contentResponse = (Map<String, Object>) candidate.get("content");
                    List<Map<String, String>> partsResponse = (List<Map<String, String>>) contentResponse.get("parts");
                    if (partsResponse != null && !partsResponse.isEmpty()) {
                        return partsResponse.get(0).get("text");
                    }
                }
            }
            return "I'm here to help with any questions about tribal crafts!";
            
        } catch (Exception e) {
            System.err.println("AI API Error: " + e.getMessage());
            throw new RuntimeException("AI service unavailable");
        }
    }
    
    private String getSmartFallbackResponse(String message) {
        String lowerMsg = message.toLowerCase();
        
        // Context-aware responses
        if (lowerMsg.contains("price") || lowerMsg.contains("cost")) {
            return "Our tribal crafts range from Rs.500 to Rs.50,000. Each piece is unique! Which product are you interested in?";
        } else if (lowerMsg.contains("shipping") || lowerMsg.contains("delivery")) {
            return "We offer free shipping on orders above Rs.2000. Delivery takes 5-7 business days across India.";
        } else if (lowerMsg.contains("return") || lowerMsg.contains("refund") || lowerMsg.contains("exchange")) {
            return "You can return products within 7 days of delivery for a full refund or exchange. Customer satisfaction is our priority!";
        } else if (lowerMsg.contains("hello") || lowerMsg.contains("hi") || lowerMsg.contains("hey")) {
            return "Namaste! Welcome to Tribal Crafts. How can I help you discover authentic Indian handicrafts today?";
        } else if (lowerMsg.contains("thank")) {
            return "You're welcome! We're honored to help you connect with India's rich tribal heritage. Is there anything else you'd like to know?";
        } else if (lowerMsg.contains("artisan") || lowerMsg.contains("maker") || lowerMsg.contains("craftsman")) {
            return "Our artisans come from Warli, Gond, Naga, Bhil, Santhal, and many other tribal communities. Each product tells their unique cultural story.";
        } else if (lowerMsg.contains("pottery") || lowerMsg.contains("clay")) {
            return "Our terracotta pottery is handcrafted using traditional coiling techniques. Each piece is sun-dried and features authentic tribal motifs.";
        } else if (lowerMsg.contains("basket") || lowerMsg.contains("weaving")) {
            return "Our bamboo baskets are woven using sustainable harvesting methods. They're perfect for storage and decor!";
        } else if (lowerMsg.contains("wood") || lowerMsg.contains("carving")) {
            return "Our wood carvings depict tribal deities and nature spirits. Each piece is hand-carved from sustainable teak wood.";
        } else if (lowerMsg.contains("textile") || lowerMsg.contains("shawl") || lowerMsg.contains("fabric")) {
            return "Our handwoven shawls use natural dyes from plants and minerals. Each piece showcases traditional tribal patterns.";
        } else if (lowerMsg.contains("jewelry") || lowerMsg.contains("bead")) {
            return "Our tribal jewelry uses natural beads, seeds, and semi-precious stones. Each piece tells a unique story of tribal heritage.";
        } else if (lowerMsg.contains("drum") || lowerMsg.contains("instrument") || lowerMsg.contains("music")) {
            return "Our traditional drums are used in ceremonies and festivals. They're made from natural materials with authentic artwork.";
        } else if (lowerMsg.contains("payment") || lowerMsg.contains("pay")) {
            return "We accept Credit Card, Debit Card, UPI, PayPal, and Cash on Delivery. All payments are secure!";
        } else if (lowerMsg.contains("track") || lowerMsg.contains("order status")) {
            return "You can track your order from the 'My Orders' section after logging into your account.";
        } else if (lowerMsg.contains("contact") || lowerMsg.contains("support") || lowerMsg.contains("help")) {
            return "You can reach us at support@tribalcrafts.com or call +91-XXXXXXXXXX. We're here Monday-Friday, 9AM-6PM.";
        } else {
            return "That's a great question! I'd love to help. Could you tell me more about what you're looking for? We have beautiful tribal crafts, or I can connect you with our support team for detailed assistance.";
        }
      
    }
}