package com.smartstore.api.v1.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("#{'${app.cors.allowed-origins}'.split(',')}")
  private List<String> allowedOrigins;

  @PostConstruct
  public void trimAllowedOrigins() {
    allowedOrigins = allowedOrigins.stream()
        .map(String::trim)
        .filter(s -> !s.isEmpty()) // 빈 문자열 제거
        .toList();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 적용
        .csrf(csrf -> csrf.disable()) // CSRF 비활성화
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**").permitAll() // 인증 필요 없는 엔드포인트
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/v1/api/**").permitAll() // API 요청은 인증 필요
            // 기타 요청 허용 (필요에 따라 수정 / TODO: JWT 추가 후 인증 수행)
            .anyRequest().permitAll())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 사용 안 함

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(allowedOrigins); // 허용할 Origin
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    config.setExposedHeaders(List.of("Set-Cookie"));
    config.setAllowCredentials(true); // 인증 포함 요청 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
