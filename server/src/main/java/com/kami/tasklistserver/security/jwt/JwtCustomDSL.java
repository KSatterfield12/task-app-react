package com.kami.tasklistserver.security.jwt;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtCustomDSL extends AbstractHttpConfigurer<JwtCustomDSL, HttpSecurity> {

    private final TokenProvider tokenProvider;

    public JwtCustomDSL(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    public static JwtCustomDSL apply(TokenProvider tokenProvider) {
        return new JwtCustomDSL(tokenProvider);
    }
}
