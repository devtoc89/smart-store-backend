package com.smartstore.api.v1.application.admin.admin.provider;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.smartstore.api.v1.common.constants.enums.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class AdminJwtProvider {

  @Value("${jwt.secret}")
  private String secretKey;
  private SecretKey key;

  @Value("${jwt.validity.access_token}")
  private long accessTokenValidity;
  @Value("${jwt.validity.refresh_token}")
  private long refreshTokenValidity;

  @PostConstruct
  public void generateSecretKey() {
    key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public long getAccessTokenValidity() {
    return accessTokenValidity;
  }

  public long getRefreshTokenValidity() {
    return refreshTokenValidity;
  }

  public String generateToken(String subject, Role role) {
    return generateAccessToken(subject, role, System.currentTimeMillis() + accessTokenValidity);
  }

  public String generateRefreshToken(String subject) {
    return generateRefreshToken(subject, System.currentTimeMillis() + refreshTokenValidity);
  }

  public String generateAccessToken(String subject, Role role, long accessTokenExpired) {
    return Jwts.builder()
        .setSubject(subject)
        .claim("role", role.name())
        .setIssuedAt(new Date())
        .setExpiration(new Date(accessTokenExpired))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken(String subject, long refreshTokenExpired) {
    return Jwts.builder()
        .setSubject(subject)
        .claim("type", "refresh")
        .setIssuedAt(new Date())
        .setExpiration(new Date(refreshTokenExpired))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public UUID getUserId(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
        .build()
        .parseClaimsJws(token)
        .getBody();

    return UUID.fromString(claims.getSubject());
  }
}