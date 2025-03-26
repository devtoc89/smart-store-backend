package com.smartstore.api.v1.common.filter;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smartstore.api.v1.application.admin.admin.provider.AdminJwtProvider;
import com.smartstore.api.v1.application.admin.admin.service.AdminService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminJwtAuthenticationFilter extends OncePerRequestFilter {

  private final AdminJwtProvider jwtProvider;
  private final AdminService adminService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String token = resolveToken(request);

    if (token != null && jwtProvider.validateToken(token)) {
      UUID adminId = jwtProvider.getUserId(token); // 또는 email

      UserDetails userDetails = adminService.loadUserByUsername(adminId.toString()); // 관리자 정보 조회

      Authentication authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities()); // 인증 객체 생성
      SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 정보 설정
    }

    chain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
  }
}