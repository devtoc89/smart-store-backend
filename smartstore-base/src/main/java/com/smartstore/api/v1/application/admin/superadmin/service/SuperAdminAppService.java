package com.smartstore.api.v1.application.admin.superadmin.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.application.admin.superadmin.dto.SuperAdminFilterRequestDTO;
import com.smartstore.api.v1.application.admin.superadmin.dto.SuperAdminResponseDTO;
import com.smartstore.api.v1.common.constants.enums.Role;
import com.smartstore.api.v1.domain.admin.entity.Admin;
import com.smartstore.api.v1.domain.admin.service.AdminDomainService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuperAdminAppService {

  @Value("${admin.super.email:}")
  private String superAdminEmail;
  @Value("${admin.super.password:}")
  private String superAdminPassword;
  @Value("${admin.super.nickname:}")
  private String superAdminNickname;

  private final AdminDomainService adminDomainService;
  private final PasswordEncoder passwordEncoder;

  private boolean hasSuperAdminConfig() {
    if (ObjectUtils.isEmpty(superAdminEmail)) {
      return false;
    }
    if (ObjectUtils.isEmpty(superAdminPassword)) {
      return false;
    }
    if (ObjectUtils.isEmpty(superAdminNickname)) {
      return false;
    }
    return true;
  }

  private Admin generateSuperAdminEntity() {
    return Admin.builder()
        .email(superAdminEmail)
        .password(passwordEncoder.encode(superAdminPassword))
        .nickname(superAdminNickname)
        .role(Role.SUPER_ADMIN)
        .isActivated(true)
        .build();

  }

  @PostConstruct
  private void initSuperAdmin() {
    if (hasSuperAdminConfig() && !adminDomainService.existsByRole(Role.SUPER_ADMIN)) {
      adminDomainService.save(generateSuperAdminEntity());
    }
  }

  @Transactional
  public boolean approveAdmin(String id) {
    return adminDomainService.approveAdmin(UUID.fromString(id));
  }

  @Transactional(readOnly = true)
  public Page<SuperAdminResponseDTO> getList(
      SuperAdminFilterRequestDTO dto, Pageable pageable) {
    return SuperAdminResponseDTO.fromVOWithPage(
        adminDomainService.findManyByCondition(
            dto.toSearchConditionVO(), pageable));
  }

}
