package com.kami.tasklistserver.dto;

public class TokenResponseDto {
    private String token;

    public TokenResponseDto() {}

    public TokenResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenResponseDto{token='[REDACTED]'}";
    }
}