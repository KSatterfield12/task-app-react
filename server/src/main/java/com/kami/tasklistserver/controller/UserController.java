package com.kami.tasklistserver.controller;

import com.kami.tasklistserver.dao.UserDao;
import com.kami.tasklistserver.dao.DaoException;
import com.kami.tasklistserver.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    // Admin only: fetch all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userDao.getUsers();
            return ResponseEntity.ok(users);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch users", "details", e.getMessage()));
        }
    }

    // Authenticated: fetch user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<?> getById(@PathVariable Long userId, Principal principal) {
        try {
            User user = userDao.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }
            return ResponseEntity.ok(user);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch user", "details", e.getMessage()));
        }
    }

    // Authenticated: update own profile
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody User modifiedUser, Principal principal) {
        try {
            User loggedInUser = userDao.getUserByUsername(principal.getName());
            if (loggedInUser == null || !loggedInUser.getId().equals(modifiedUser.getId())) {
                throw new AccessDeniedException("Access denied");
            }
            User updated = userDao.updateUser(modifiedUser);
            return ResponseEntity.ok(updated);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Update failed", "details", e.getMessage()));
        }
    }
}