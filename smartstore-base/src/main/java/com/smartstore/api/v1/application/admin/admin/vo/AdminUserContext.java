package com.smartstore.api.v1.application.admin.admin.vo;

import java.util.UUID;

import com.smartstore.api.v1.common.constants.enums.Role;
import com.smartstore.api.v1.domain.admin.entity.Admin;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
public class AdminUserContext {
  private final UUID id;
  private final String email;
  private final String password;
  private final String nickname;
  private final Role role;
  private final Boolean isActivated;
  private final Integer loginFailCount;

  public static AdminUserContext fromEntity(Admin admin) {
    // 민감한 정보는 삭제한 context 제공
    // 비밀번호는 제거되므로 주위!
    return AdminUserContext.builder()
        .id(admin.getId())
        .email(admin.getEmail())
        .password("")
        .nickname(admin.getNickname())
        .role(admin.getRole())
        .isActivated(admin.getIsActivated())
        .build();
  }

}