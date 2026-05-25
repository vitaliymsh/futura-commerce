package com.futura.commerce.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token utility
 *
 * @author Vitalii
 */
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret:futura-default-secret-key-for-jwt-token-generation-2026-very-secure}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    @Value("${jwt.tokenHeader:Authorization}")
    private String tokenHeader;

    /**
     * Generate JWT token from user authentication details
     */
    /**
     * Generate JWT token from plain username string
     */
    public String generateTokenFromUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private byte[] getSigningKeyBytes() {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            return md.digest(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            return secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    /**
     * Build token payload with expiration and signature
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, getSigningKeyBytes())
                .compact();
    }

    /**
     * Extract username subject from token
     */
    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKeyBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verify whether token has expired
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expireDate = Jwts.parser()
                    .setSigningKey(getSigningKeyBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expireDate.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Validate token against user details
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Verify token signature and integrity
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKeyBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Refresh existing valid token
     */
    public String refreshToken(String token) {
        try {
            if (!validateToken(token)) {
                return null;
            }
            String username = getUsernameFromToken(token);
            if (username == null) {
                return null;
            }
            Map<String, Object> claims = new HashMap<>();
            return createToken(claims, username);
        } catch (Exception e) {
            return null;
        }
    }
}
