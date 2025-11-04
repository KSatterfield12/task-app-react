package com.kami.tasklistserver.payload;

import com.kami.tasklistserver.model.UserDto;
import java.util.Objects;

public class JwtResponse {

    private String token;
    private UserDto user;

    public JwtResponse() {}

    public JwtResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "JwtResponse{token='[PROTECTED]', user=" + (user != null ? user.getUsername() : "null") + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtResponse)) return false;
        JwtResponse that = (JwtResponse) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, user);
    }
}