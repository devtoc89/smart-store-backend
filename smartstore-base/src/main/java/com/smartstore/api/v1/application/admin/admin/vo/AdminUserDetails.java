package com.smartstore.api.v1.application.admin.admin.vo;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AdminUserDetails implements UserDetails {

  private final transient AdminUserContext adminContext;

  public AdminUserDetails(AdminUserContext adminContext) {
    this.adminContext = adminContext;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + adminContext.getRole().name())); // 예: ROLE_ADMIN
    return authorities;
  }

  @Override
  public String getPassword() {
    return adminContext.getPassword();
  }

  @Override
  public String getUsername() {
    return adminContext.getEmail(); // email을 username으로 사용
  }

  @Override
  public boolean isAccountNonExpired() {
    return adminContext.getIsActivated(); // isActivated가 true일 때만 계정 유효
  }

  @Override
  public boolean isAccountNonLocked() {
    return adminContext.getLoginFailCount() < 5; // 로그인 실패 횟수가 5 이상이면 계정 잠금
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // 비밀번호 만료 여부 설정 (이 예시에서는 만료되지 않음)
  }

  @Override
  public boolean isEnabled() {
    return adminContext.getIsActivated(); // 계정이 활성화되어 있으면 true
  }

  public AdminUserContext getAdminContext() {
    return adminContext;
  }
}
