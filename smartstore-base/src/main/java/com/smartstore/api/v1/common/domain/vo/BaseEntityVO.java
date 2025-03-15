package com.smartstore.api.v1.common.domain.vo;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.smartstore.api.v1.common.domain.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseEntityVO {
  private final UUID id;
  private final Boolean isDeleted;
  private final ZonedDateTime createdAt;
  private final ZonedDateTime updatedAt;
  private final ZonedDateTime deletedAt;

  public static <T extends BaseEntity> BaseEntityVO fromEntity(T entity) {
    return new BaseEntityVO(
        entity.getId(),
        entity.getIsDeleted(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getDeletedAt());
  }
}
