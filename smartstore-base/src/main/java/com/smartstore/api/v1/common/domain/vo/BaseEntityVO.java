package com.smartstore.api.v1.common.domain.vo;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.smartstore.api.v1.common.domain.entity.BaseEntity;

import lombok.Getter;

// use it only compound
@Getter
public class BaseEntityVO {
  private final UUID id;
  private final Boolean isDeleted;
  private final ZonedDateTime createdAt;
  private final ZonedDateTime updatedAt;
  private final ZonedDateTime deletedAt;

  protected <T extends BaseEntity> BaseEntityVO(T entity) {
    this.id = entity.getId();
    this.isDeleted = entity.getIsDeleted();
    this.createdAt = entity.getCreatedAt();
    this.updatedAt = entity.getUpdatedAt();
    this.deletedAt = entity.getDeletedAt();
  }

  public static <T extends BaseEntity> BaseEntityVO fromEntity(T entity) {
    return new BaseEntityVO(entity);
  }
}
