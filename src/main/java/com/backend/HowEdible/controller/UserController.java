package com.backend.HowEdible.controller;

import com.backend.HowEdible.dto.UserLoginRequest;
import com.backend.HowEdible.repository.UserRepository; // this is what's imported new

import com.backend.HowEdible.model.User;
import com.backend.HowEdible.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody User user) {
        try {
            userService.registerUser(user); // Use registerUser to encrypt the password
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error registering user: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest loginRequest) {
        // Find the user by username
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Validate the password
        if (!userService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Generate a successful response
        return ResponseEntity.ok("Login successful");
    }
    
    
    //to login the users
    @GetMapping("/profile/{username}")
    public ResponseEntity<User> getUserProfile(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // If user not found
        }
        return ResponseEntity.ok(user); // Send user data
    }
    
    // this is the new mapping endpoint that if it causes errors delete it **video** related
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}/videos")
    public ResponseEntity<User> getUserWithVideos(@PathVariable Long userId) {
        User user = userRepository.findByIdWithVideos(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }


}
