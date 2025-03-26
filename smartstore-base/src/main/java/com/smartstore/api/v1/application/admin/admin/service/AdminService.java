package com.smartstore.api.v1.application.admin.admin.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.application.admin.admin.dto.AdminLoginRequestDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminLoginResponseDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminSignupRequestDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminTokenRefreshResponseDTO;
import com.smartstore.api.v1.application.admin.admin.entity.Admin;
import com.smartstore.api.v1.application.admin.admin.entity.AdminDetails;
import com.smartstore.api.v1.application.admin.admin.entity.AdminToken;
import com.smartstore.api.v1.application.admin.admin.provider.AdminJwtProvider;
import com.smartstore.api.v1.application.admin.admin.repository.AdminRepository;
import com.smartstore.api.v1.application.admin.admin.repository.AdminTokenRepository;
import com.smartstore.api.v1.common.constants.enums.Role;
import com.smartstore.api.v1.common.exception.BadRequestException;
import com.smartstore.api.v1.common.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {
  private final AdminRepository adminRepository;
  private final AdminTokenRepository adminTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminJwtProvider jwtProvider;

  public void registerAdmin(AdminSignupRequestDTO request) {
    if (adminRepository.existsByEmail(request.getEmail())) {
      throw new BadRequestException("이미 등록된 이메일입니다.");
    }

    Admin admin = Admin.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .nickname(request.getNickname())
        .role(Role.ADMIN)
        .isActivated(false)
        .loginFailCount(0)
        .createdAt(ZonedDateTime.now()) // BaseEntity 필드
        .build();

    adminRepository.save(admin);
  }

  @Transactional
  public AdminLoginResponseDTO login(AdminLoginRequestDTO request) {
    Admin admin = adminRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UnauthorizedException("존재하지 않는 계정입니다."));

    // TODO: super admin 생성, 관리자 계정 승인 프로세스
    // if (!admin.getIsActivated().equals(true)) {
    // throw new ForbiddenException("승인되지 않은 관리자 계정입니다.");
    // }

    if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
      admin.setLoginFailCount(admin.getLoginFailCount() + 1);
      adminRepository.save(admin);
      throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
    }

    admin.setLoginFailCount(0);
    admin.setLastLoginAt(ZonedDateTime.now());
    admin.setToken(null); // 관계 끊기

    adminRepository.save(admin);

    // ✅ Access / Refresh Token 발급
    String accessToken = jwtProvider.generateToken(admin);
    String refreshToken = jwtProvider.generateRefreshToken(admin);
    ZonedDateTime refreshExpiresAt = ZonedDateTime.now().plusDays(7);

    // ✅ RefreshToken 저장
    adminTokenRepository.save(AdminToken.builder()
        .admin(admin)
        .refreshToken(refreshToken)
        .expiresAt(refreshExpiresAt)
        .build());

    return new AdminLoginResponseDTO(accessToken, refreshToken);
  }

  @Transactional
  public void logout(UUID adminId) {
    // TODO: 블랙리스트 등록
    adminTokenRepository.deleteByAdminId(adminId);
  }

  public AdminTokenRefreshResponseDTO refreshAccessToken(String refreshToken) {
    if (!jwtProvider.validateToken(refreshToken)) {
      throw new UnauthorizedException("유효하지 않은 리프레시 토큰입니다.");
    }

    UUID adminId = jwtProvider.getUserId(refreshToken);
    var admin = ((AdminDetails) loadUserByUsername(adminId.toString())).getAdmin();
    // Admin admin = adminRepository.findById(adminId)
    // .orElseThrow(() -> new UnauthorizedException("존재하지 않는 관리자입니다."));

    AdminToken token = adminTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new UnauthorizedException("등록되지 않은 리프레시 토큰입니다."));

    if (token.isExpired()) {
      throw new UnauthorizedException("리프레시 토큰이 만료되었습니다.");
    }

    // TODO: accessToken Blacklist

    String newAccessToken = jwtProvider.generateToken(admin);
    String newRefreshToken = jwtProvider.generateRefreshToken(admin);

    token.setRefreshToken(newRefreshToken);
    token.setExpiresAt(ZonedDateTime.now().plusDays(7));
    adminTokenRepository.save(token);

    return new AdminTokenRefreshResponseDTO(newAccessToken, newRefreshToken);
  }

  public Admin findByIdOrExcept(String id) {
    return adminRepository.findById(UUID.fromString(id))
        .orElseThrow(() -> new UsernameNotFoundException("invalid token"));
  }

  @Override
  @Cacheable(value = "AdminDetails", key = "#p0")
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    return new AdminDetails(findByIdOrExcept(id));
  }

}
