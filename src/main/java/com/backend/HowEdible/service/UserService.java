package com.backend.HowEdible.service;

import com.backend.HowEdible.model.User;
import com.backend.HowEdible.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // constructor injection for best practice)
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // register a new user
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // find a user by user name
    public User findByUsername(String username) {
        return userRepository.findByUsername(username); // no incorrect cast
    }

    // verify password
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // authenticate user
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && verifyPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
