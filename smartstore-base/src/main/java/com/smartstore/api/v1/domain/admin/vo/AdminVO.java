package com.smartstore.api.v1.domain.admin.vo;

import java.time.ZonedDateTime;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.domain.admin.entity.Admin;
import com.smartstore.api.v1.domain.common.vo.BaseEntityVO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminVO {
  private final BaseEntityVO base;
  private final String email;
  private final String nickname;
  private final Boolean isActivated;
  private final Integer loginFailCount;
  private final ZonedDateTime lastLoginAt;

  public static AdminVO fromEntity(Admin entity) {
    return AdminVO.builder()
        .base(BaseEntityVO.fromEntity(entity))
        .email(entity.getEmail())
        .nickname(entity.getNickname())
        .isActivated(entity.getIsActivated())
        .loginFailCount(entity.getLoginFailCount())
        .lastLoginAt(entity.getLastLoginAt())
        .build();
  }

  public static Page<AdminVO> fromEntityWithPage(Page<Admin> entities) {
    return entities.map(v -> fromEntity(v));
  }
}
