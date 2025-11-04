package com.kami.tasklistserver.service;

import com.kami.tasklistserver.security.JwtUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.kami.tasklistserver.dto.RegisterUserDto;
import com.kami.tasklistserver.model.User;
import com.kami.tasklistserver.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Authentication authenticate(String username, String password) {
        String normalized = username != null ? username.trim().toLowerCase() : null;
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalized, password)
        );
    }

    public String authenticateAndGenerateToken(String username, String password) {
        Authentication auth = authenticate(username, password);
        return jwtUtils.generateToken(auth);
    }

    public boolean registerUser(RegisterUserDto dto) {
        String normalizedUsername = dto.getUsername().trim().toLowerCase();

        if (userRepository.existsByUsername(normalizedUsername)) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(normalizedUsername);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setRole("ROLE_USER");

        userRepository.save(newUser);
        return true;
    }
}