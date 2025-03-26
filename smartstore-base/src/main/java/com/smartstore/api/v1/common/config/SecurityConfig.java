package com.smartstore.api.v1.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.smartstore.api.v1.application.admin.admin.AdminPublicMeta;
import com.smartstore.api.v1.common.constants.enums.Role;
import com.smartstore.api.v1.common.filter.AdminJwtAuthenticationFilter;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AdminJwtAuthenticationFilter adminJwtFilter)
      throws Exception {
    http
        // .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 적용
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 적용
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http
        .securityMatcher("/**")
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/swagger/**").permitAll()
            .requestMatchers(AdminPublicMeta.SIGNUP_FULL_PATH, AdminPublicMeta.LOGIN_FULL_PATH).permitAll()
            .anyRequest().hasRole(Role.ADMIN.name()))
        .addFilterBefore(adminJwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    log.info("allowed orgins:{}", allowedOrigins);

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
