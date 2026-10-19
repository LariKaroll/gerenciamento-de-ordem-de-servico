package com.projeto.service_user.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final SecretKey signingKey;
    private final long expirationMinutes;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secretBase64,
            @Value("${app.jwt.expiration-minutes}") long expirationMinutes) {
        this.expirationMinutes = expirationMinutes;
        byte[] keyBytes = Base64.getDecoder().decode(secretBase64);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public long getExpirationMinutes() {
        return this.expirationMinutes;
    }

    public String generateToken(UUID id, String perfil) {
        long expirationMillis = expirationMinutes * 60 * 1000;

        return Jwts.builder()
                .subject(id.toString())
                .claim("perfil", perfil)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(signingKey)
                .compact();
    }

    public TokenDados extrairEValidarToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            UUID id = UUID.fromString(claims.getSubject());
            String perfil = claims.get("perfil", String.class);
            
            return new TokenDados(id, perfil, true);
        } catch (JwtException | IllegalArgumentException e) {
            return new TokenDados(null, null, false);
        }
    }

    public record TokenDados(UUID id, String perfil, boolean valido) {}
}