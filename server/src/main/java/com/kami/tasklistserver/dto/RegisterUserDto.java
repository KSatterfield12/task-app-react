package com.kami.tasklistserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterUserDto {

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotEmpty(message = "Please confirm your password")
    private String confirmPassword;

    @NotEmpty(message = "Please select a role for this user.")
    @Pattern(regexp = "user|admin|guest", message = "Invalid role")
    private String role;

    public RegisterUserDto() {}

    public RegisterUserDto(String username, String password, String confirmPassword, String role) {
        this.username = username != null ? username.trim().toLowerCase() : null;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role != null ? role.trim().toLowerCase() : null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username.trim().toLowerCase() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role != null ? role.trim().toLowerCase() : null;
    }

    @Override
    public String toString() {
        return "RegisterUserDto{username='" + username + "', role='" + role + "', password='[PROTECTED]'}";
    }
}