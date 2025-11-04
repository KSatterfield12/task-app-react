package com.kami.tasklistserver.model;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    ADMIN, USER, GUEST;

    public static UserRole from(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or blank");
        }
        try {
            return UserRole.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public static List<String> allRoles() {
        return Arrays.stream(UserRole.values())
                .map(Enum::name)
                .toList();
    }
}