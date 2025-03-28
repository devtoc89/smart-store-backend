package com.smartstore.api.v1.application.admin.admin.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartstore.api.v1.application.admin.admin.vo.AdminUserContext;

public class AdminDetails implements UserDetails {

  private final transient AdminUserContext adminContext;

  public AdminDetails(AdminUserContext adminContext) {
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
    // TODO: 개정 활성화 프로세스
    return true;
    // return adminContextHoler.getIsActivated(); // isActivated가 true일 때만 계정 유효
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
    return adminContext; // 원본 Admin 객체를 반환
  }
}
