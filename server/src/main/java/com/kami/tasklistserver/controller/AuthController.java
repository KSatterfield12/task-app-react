package com.kami.tasklistserver.controller;

import com.kami.tasklistserver.dto.RegisterUserDto;
import com.kami.tasklistserver.security.JwtUtils;
import com.kami.tasklistserver.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Username and password are required"));
        }

        try {
            Authentication authentication = authService.authenticate(username, password);
            String token = jwtUtils.generateToken(authentication);

            // TaskClient expects JSON with a "token" field
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto dto, BindingResult result) {
        // Basic validation
        if (dto.getUsername() == null || dto.getPassword() == null || dto.getConfirmPassword() == null || dto.getRole() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "All fields are required"));
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Passwords do not match"));
        }

        boolean success = authService.registerUser(dto);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Username already exists"));
        }
    }
}