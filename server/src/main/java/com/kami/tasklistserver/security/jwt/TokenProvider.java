package com.kami.tasklistserver.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";

    private final String base64Secret;
    private final long tokenValidityInMilliseconds;
    private Key key;

    public TokenProvider(
            @Value("${jwt.base64-secret}") String base64Secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.base64Secret = base64Secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        return createToken(authentication);
    }

    public String createToken(Authentication authentication) {
        // Step 1: Extract and format authorities
        List<String> authoritiesList = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth : "ROLE_" + auth)
                .collect(Collectors.toList());

        String authorities = String.join(",", authoritiesList);

        // Step 2: Log for debugging
        log.debug("Creating token for user '{}' with authorities: {}", authentication.getName(), authorities);

        long now = System.currentTimeMillis();
        Date expiration = new Date(now + tokenValidityInMilliseconds);

        // Step 3: Include 'auth' claim in JWT
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Signature error: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("JWT token expired.");
            log.trace("Expiration error: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported error: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token is malformed.");
            log.trace("Malformed error: {}", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String authoritiesString = claims.get(AUTHORITIES_KEY, String.class);
        List<GrantedAuthority> authorities = Arrays.stream(Optional.ofNullable(authoritiesString).orElse("")
                        .split(","))
                .filter(auth -> !auth.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}