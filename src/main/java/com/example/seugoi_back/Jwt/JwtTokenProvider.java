package com.example.seugoi_back.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long userCode, String email) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + 1000L * 60 * 60 * 24); // 1일

        return Jwts.builder()
            .subject(String.valueOf(userCode))
            .claim("email", email)
            .issuedAt(now)
            .expiration(expire)
            .signWith(secretKey)
            .compact();
    }

    public String createRefreshToken(Long userCode) {
        Date now = new Date();

        return Jwts.builder()
            .subject(String.valueOf(userCode))
            .issuedAt(now)
            .expiration(new Date(now.getTime() + 1000L * 60 * 60 * 24 * 14)) // 14일
            .signWith(secretKey)
            .compact();
    }

    public Long getUserCode(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        return Long.valueOf(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
