package com.smartstore.api.v1.application.admin.admin.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstore.api.v1.application.admin.admin.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
  boolean existsByEmail(String email);

  Optional<Admin> findByEmail(String email);
}
