package com.smartstore.api.v1.domain.admin.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstore.api.v1.common.constants.enums.Role;
import com.smartstore.api.v1.domain.admin.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
  boolean existsByEmail(String email);

  Optional<Admin> findByEmail(String email);

  boolean existsByRole(Role role);

  // Specification + Pageable을 사용한 페이징 처리
  @SuppressWarnings("null")
  Page<Admin> findAll(Specification<Admin> spec, Pageable pageable);
}
