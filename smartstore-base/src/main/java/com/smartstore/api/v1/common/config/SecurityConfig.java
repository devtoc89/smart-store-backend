package com.smartstore.api.v1.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // TODO: 보안 롤 결정되면 컨트롤러 별 권한 설정 필요

    // TODO:JWT 토큰 인증 필요

    http
        .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (POST 요청 가능하게)
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // 모든 요청 허용

    return http.build();
  }
}