package com.tribalcrafts.tribalcrafts_backend.repository;

import com.tribalcrafts.tribalcrafts_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailAndPassword(String email, String password);
    
    // Add this method for admin dashboard
    long count();
}