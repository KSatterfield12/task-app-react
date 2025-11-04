package com.kami.tasklistserver.service;

import com.kami.tasklistserver.model.User;

public interface UserService {
    boolean existsByUsername(String username);
    User getUserById(Long id);
    void createUser(String username, String password, String role);
}
