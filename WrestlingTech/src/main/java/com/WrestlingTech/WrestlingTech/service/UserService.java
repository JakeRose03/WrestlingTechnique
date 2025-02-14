package com.WrestlingTech.WrestlingTech.service;

import com.WrestlingTech.WrestlingTech.model.User;
import com.WrestlingTech.WrestlingTech.model.Role;
import com.WrestlingTech.WrestlingTech.Repos.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String email, String password, Role role) {
        if (userRepository.findByUsername(username) != null || userRepository.findByEmail(email) != null) {
            throw new RuntimeException("User already exists!");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, email, role);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }
}
