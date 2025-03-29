package com.smartstore.api.v1.application.admin.admin.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartstore.api.v1.application.admin.admin.vo.AdminUserContext;
import com.smartstore.api.v1.application.admin.admin.vo.AdminUserDetails;
import com.smartstore.api.v1.domain.admin.service.AdminDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsAppService implements UserDetailsService {

  private final AdminDomainService adminDomainService;

  @Override
  @Cacheable(value = "AdminDetails", key = "#p0")
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    return new AdminUserDetails(AdminUserContext.fromEntity(adminDomainService.findByIdOrExcept(id)));
  }
}