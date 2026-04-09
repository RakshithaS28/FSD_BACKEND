package com.tribalcrafts.tribalcrafts_backend.controller;

import com.tribalcrafts.tribalcrafts_backend.dto.LoginRequest;
import com.tribalcrafts.tribalcrafts_backend.dto.SignupRequest;
import com.tribalcrafts.tribalcrafts_backend.entity.User;
import com.tribalcrafts.tribalcrafts_backend.repository.UserRepository;
import com.tribalcrafts.tribalcrafts_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public User signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
    
    @PutMapping("/assign-role")
    public Map<String, Object> assignRole(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        String role = request.get("role").toString();
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        
        // Check if user already has a role
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            throw new RuntimeException("User already has a role assigned: " + user.getRole());
        }
        
        user.setRole(role);
        userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Role assigned successfully");
        response.put("email", user.getEmail());
        response.put("role", role);
        response.put("userId", user.getId());
        response.put("username", user.getFirstName() + " " + user.getLastName());
        
        return response;
    }
    
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}