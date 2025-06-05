package com.microservices.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key jwtSecretKey;
    private final long jwtExpirationMs;

    public JwtUtils(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration-ms}") long expirationMs
    ) {
        // Create a signing key from the secret
        this.jwtSecretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = expirationMs;
    }

    public String generateJwtToken(String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                   .setSubject(username)
                   .claim("role", role)
                   .setIssuedAt(now)
                   .setExpiration(expiry)
                   .signWith(jwtSecretKey, SignatureAlgorithm.HS256)
                   .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(jwtSecretKey)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public String getRoleFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                            .setSigningKey(jwtSecretKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        return claims.get("role", String.class);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // log: token expired
        } catch (JwtException | IllegalArgumentException e) {
            // log: invalid token
        }
        return false;
    }
}
