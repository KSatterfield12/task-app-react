package com.kami.tasklistserver.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public LoginDto() {}

    public LoginDto(String username, String password) {
        this.username = username != null ? username.trim() : null;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDto{username='" + username + "', password='[PROTECTED]'}";
    }
}