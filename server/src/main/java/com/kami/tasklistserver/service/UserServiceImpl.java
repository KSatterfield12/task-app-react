package com.kami.tasklistserver.service;

import com.kami.tasklistserver.model.User;
import com.kami.tasklistserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean existsByUsername(String username) {
        String normalized = username.trim().toLowerCase();
        return userRepository.existsByUsername(normalized);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public void createUser(String username, String password, String role) {
        String normalized = username.trim().toLowerCase();

        if (existsByUsername(normalized)) {
            throw new IllegalArgumentException("Username already exists: " + normalized);
        }

        User user = new User();
        user.setUsername(normalized);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);
        log.info("Created new user: '{}'", normalized);
    }
}