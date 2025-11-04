package com.kami.tasklistserver.security;

import com.kami.tasklistserver.security.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) ||
                path.equals("/ping") || path.startsWith("/auth") || path.startsWith("/api/public")) {
            log.debug("Skipping JWT validation for public or preflight request: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = parseToken(request);

        if (StringUtils.hasText(token)) {
            log.debug("JWT found in header: {}...", token.substring(0, Math.min(10, token.length())));

            if (tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                log.debug("Authenticated user: {}", authentication.getName());
                log.debug("Granted authorities: {}", authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.debug("Token failed validation");
                throw new BadCredentialsException("Invalid JWT token");
            }
        } else {
            log.debug("No JWT found in request header");
        }

        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (StringUtils.hasText(bearer) && bearer.startsWith("Bearer "))
                ? bearer.substring(7) : null;
    }
}