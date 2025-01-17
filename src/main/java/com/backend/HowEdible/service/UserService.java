package com.backend.HowEdible.service;

import com.backend.HowEdible.model.User;
import com.backend.HowEdible.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Save user details
    public User save(User user) {
        // Perform additional business logic here, if needed
        return userRepository.save(user);
    }

    // Find a user by their username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // Retrieve user by username
    }

    // Verify the user's password
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
