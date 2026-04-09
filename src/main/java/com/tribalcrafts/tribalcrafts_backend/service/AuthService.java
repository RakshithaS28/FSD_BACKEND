package com.tribalcrafts.tribalcrafts_backend.service;

import com.tribalcrafts.tribalcrafts_backend.dto.LoginRequest;
import com.tribalcrafts.tribalcrafts_backend.dto.SignupRequest;
import com.tribalcrafts.tribalcrafts_backend.entity.User;
import com.tribalcrafts.tribalcrafts_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User signup(SignupRequest request) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists with this email");
        }

        // Create new user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // In production, encrypt this!
        user.setRole(request.getRole()); // Can be null
        user.setStatus("Active");

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }
        return userOpt.get();
    }
}