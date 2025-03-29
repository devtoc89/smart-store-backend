package com.smartstore.api.v1.application.admin.admin.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartstore.api.v1.application.admin.admin.entity.Admin;
import com.smartstore.api.v1.application.admin.admin.repository.AdminRepository;
import com.smartstore.api.v1.application.admin.admin.vo.AdminUserContext;
import com.smartstore.api.v1.application.admin.admin.vo.AdminUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDomainService implements UserDetailsService {
  private final AdminRepository adminRepository;

  public Admin findByIdOrExcept(String id) {
    return adminRepository.findById(UUID.fromString(id))
        .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 사용자입니다."));
  }

  public Admin save(Admin entity) {
    return adminRepository.save(entity);
  }

  public boolean existsByEmail(String email) {
    return adminRepository.existsByEmail(email);
  }

  public Optional<Admin> findByEmail(String email) {
    return adminRepository.findByEmail(email);
  }

  @Override
  @Cacheable(value = "AdminDetails", key = "#p0")
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    return new AdminUserDetails(AdminUserContext.fromEntity(findByIdOrExcept(id)));
  }
}
