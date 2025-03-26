package com.smartstore.api.v1.application.admin.admin.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstore.api.v1.application.admin.admin.entity.Admin;
import com.smartstore.api.v1.application.admin.admin.entity.AdminToken;

public interface AdminTokenRepository extends JpaRepository<AdminToken, UUID> {
  Optional<AdminToken> findByRefreshToken(String refreshToken);

  Optional<AdminToken> findByAdmin(Admin admin);

  void deleteByAdmin(Admin admin);

  void deleteByAdminId(UUID adminId);
}