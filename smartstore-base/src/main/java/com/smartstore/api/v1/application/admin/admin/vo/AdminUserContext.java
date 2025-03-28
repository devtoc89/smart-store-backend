package com.smartstore.api.v1.application.admin.admin.vo;

import java.util.UUID;

import com.smartstore.api.v1.application.admin.admin.entity.Admin;
import com.smartstore.api.v1.common.constants.enums.Role;

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
    return AdminUserContext.builder()
        .id(admin.getId())
        .email(admin.getEmail())
        .password(admin.getPassword())
        .nickname(admin.getNickname())
        .role(admin.getRole())
        .isActivated(admin.getIsActivated())
        .build();
  }

}