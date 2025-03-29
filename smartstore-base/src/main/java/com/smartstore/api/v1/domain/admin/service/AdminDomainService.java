package com.smartstore.api.v1.domain.admin.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.common.constants.enums.Role;
import com.smartstore.api.v1.domain.admin.entity.Admin;
import com.smartstore.api.v1.domain.admin.repository.AdminRepository;
import com.smartstore.api.v1.domain.admin.vo.AdminFilterConditionVO;
import com.smartstore.api.v1.domain.admin.vo.AdminVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDomainService {
  private final AdminRepository adminRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Admin findByIdOrExcept(String id) {
    return adminRepository.findById(UUID.fromString(id))
        .orElseThrow(() -> new UsernameNotFoundException(""));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean existsByEmail(String email) {
    return adminRepository.existsByEmail(email);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Optional<Admin> findByEmail(String email) {
    return adminRepository.findByEmail(email);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean existsByRole(Role role) {
    return adminRepository.existsByRole(role);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<AdminVO> findManyByCondition(AdminFilterConditionVO condition, Pageable pageable) {
    return AdminVO.fromEntityWithPage(adminRepository.findAll(condition.toSpecification(), pageable));
  }

  // TODO: 유저에 대한 처리는 포괄 처리 없이, 정확한 스팩대로만 동작하도록 할 것!
  @Transactional(propagation = Propagation.REQUIRED)
  public Admin save(Admin entity) {
    return adminRepository.save(entity);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public boolean approveAdmin(UUID id) {
    return adminRepository.findById(id)
        .map(admin -> {
          if (admin.getRole() == Role.ADMIN && Boolean.TRUE.equals(admin.getIsActivated())) {
            return false;
          }
          admin.setIsActivated(true);
          adminRepository.save(admin);
          return true;
        })
        .orElse(false);
  }
}
