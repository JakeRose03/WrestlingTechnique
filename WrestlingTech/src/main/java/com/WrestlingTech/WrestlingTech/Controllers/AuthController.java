package com.WrestlingTech.WrestlingTech.Controllers;

import com.WrestlingTech.WrestlingTech.model.User;
import com.WrestlingTech.WrestlingTech.model.Role;
import com.WrestlingTech.WrestlingTech.security.JwtUtil;
import com.WrestlingTech.WrestlingTech.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        try {
            // Validate role
            String roleString = request.get("role").toUpperCase();
            Role role;
            try {
                role = Role.valueOf(roleString);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid role. Must be COACH, ATHLETE, or PARENT."));
            }

            User user = userService.registerUser(
                    request.get("username"),
                    request.get("email"),
                    request.get("password"),
                    role
            );
            return ResponseEntity.ok(Map.of("message", "User registered successfully!", "userId", user.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        Optional<User> userOpt = userService.findByUsername(request.get("username"));

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Check password
            if (!passwordEncoder.matches(request.get("password"), user.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
            }

            // Generate token
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of("token", token, "username", user.getUsername(), "role", user.getRole()));
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Invalid username or password"));
    }
}

