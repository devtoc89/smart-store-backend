package com.smartstore.api.v1.application.admin.admin.service;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.application.admin.admin.dto.AdminLoginRequestDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminLoginResponseDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminSignupRequestDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminTokenRefreshResponseDTO;
import com.smartstore.api.v1.application.admin.admin.entity.Admin;
import com.smartstore.api.v1.application.admin.admin.entity.AdminToken;
import com.smartstore.api.v1.application.admin.admin.provider.AdminJwtProvider;
import com.smartstore.api.v1.application.admin.admin.repository.AdminTokenRepository;
import com.smartstore.api.v1.application.admin.admin.vo.AdminUserDetails;
import com.smartstore.api.v1.common.constants.enums.Role;
import com.smartstore.api.v1.common.exception.BadRequestException;
import com.smartstore.api.v1.common.exception.UnauthorizedException;
import com.smartstore.api.v1.common.utils.date.DateUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAppService {
  private final AdminTokenRepository adminTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final AdminJwtProvider jwtProvider;
  private final AdminDomainService adminDomainService;

  private record TokenContext(String accessToken, long accessTokenExpiredAt, String refreshToken,
      long refreshTokenExpiredAt) {
  }

  private TokenContext generateAllTokenContext(UUID id, Role role) {
    var idStr = id.toString();
    var now = System.currentTimeMillis();
    var accessTokenExpiredAt = jwtProvider.getAccessTokenValidity() + now;
    var refreshTokenExpiredAt = jwtProvider.getRefreshTokenValidity() + now;
    // ✅ Access / Refresh Token 발급
    String accessToken = jwtProvider.generateAccessToken(idStr, role, accessTokenExpiredAt);
    String refreshToken = jwtProvider.generateRefreshToken(idStr, refreshTokenExpiredAt);

    return new TokenContext(accessToken, accessTokenExpiredAt, refreshToken, refreshTokenExpiredAt);
  }

  @Transactional
  public void registerAdmin(AdminSignupRequestDTO request) {
    if (adminDomainService.existsByEmail(request.getEmail())) {
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

    adminDomainService.save(admin);
  }

  @Transactional
  public AdminLoginResponseDTO login(AdminLoginRequestDTO request) {
    Admin admin = adminDomainService.findByEmail(request.getEmail())
        .orElseThrow(() -> new UnauthorizedException("로그인 정보가 일치하지 않습니다."));

    // TODO: super admin 생성, 관리자 계정 승인 프로세스
    // if (!admin.getIsActivated().equals(true)) {
    // throw new ForbiddenException("승인되지 않은 관리자 계정입니다.");
    // }

    if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
      admin.setLoginFailCount(admin.getLoginFailCount() + 1);
      adminDomainService.save(admin);
      throw new UnauthorizedException("로그인 정보가 일치하지 않습니다.");
    }

    var allTokenConext = generateAllTokenContext(admin.getId(), admin.getRole());

    admin.setLoginFailCount(0);
    admin.setLastLoginAt(ZonedDateTime.now());
    admin.setToken(AdminToken.builder()
        .admin(admin)
        .refreshToken(allTokenConext.refreshToken)
        .expiresAt(DateUtil.fromMillisecondWithDefaultZone(allTokenConext.refreshTokenExpiredAt))
        .build()); // 관계 끊기

    adminDomainService.save(admin);

    return new AdminLoginResponseDTO(allTokenConext.accessToken, allTokenConext.refreshToken);
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
    var adminUserContext = ((AdminUserDetails) adminDomainService.loadUserByUsername(adminId.toString()))
        .getAdminContext();

    AdminToken token = adminTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new UnauthorizedException("유효하지 않은 리프레시 토큰입니다."));

    if (token.isExpired()) {
      throw new UnauthorizedException("유효하지 않은 리프레시 토큰입니다.");
    }
    var allTokenConext = generateAllTokenContext(adminUserContext.getId(), adminUserContext.getRole());
    // // TODO: accessToken Blacklist

    token.setRefreshToken(allTokenConext.refreshToken);
    token.setExpiresAt(DateUtil.fromMillisecondWithDefaultZone(allTokenConext.refreshTokenExpiredAt));
    adminTokenRepository.save(token);

    return new AdminTokenRefreshResponseDTO(allTokenConext.accessToken, allTokenConext.refreshToken);
  }

}
