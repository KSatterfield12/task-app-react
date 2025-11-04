package com.kami.tasklistserver.model;

import java.util.Objects;

public class UserDto {

    private Long id;
    private String username;

    public UserDto() {
    }

    public UserDto(Long id, String username) {
        this.id = id;
        this.username = username != null ? username.trim() : null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }

    @Override
    public String toString() {
        return "UserDto{id=" + id + ", username='" + username + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}