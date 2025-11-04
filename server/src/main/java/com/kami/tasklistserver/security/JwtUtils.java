package com.kami.tasklistserver.security;

import com.kami.tasklistserver.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.base64-secret}")
    public String jwtSecret;

    @Value("${jwt.token-validity-in-seconds}")
    public long tokenValidityInSeconds;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenValidityInSeconds * 1000);

        String role = "ROLE_" + userPrincipal.getRole().trim().toUpperCase();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim("auth", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT signature.");
            log.debug("Signature exception: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token.");
            log.debug("Expiration exception: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token.");
            log.debug("Unsupported token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT token is malformed or empty.");
            log.debug("Token error: {}", e.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            log.debug("No authentication found in security context");
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            log.debug("Authenticated user found: {}", userDetails.getUsername());
            return userDetails.getUsername();
        } else if (principal instanceof String username) {
            log.debug("Authenticated username from string: {}", username);
            return username;
        }

        return null;
    }
}