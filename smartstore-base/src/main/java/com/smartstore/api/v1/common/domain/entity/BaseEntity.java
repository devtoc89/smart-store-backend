package com.smartstore.api.v1.common.domain.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

  @Id
  @Column(updatable = false, nullable = false) // 기본 키는 변경할 수 없음
  private UUID id;

  @Column(nullable = false)
  private Boolean isDeleted; // 기본값 false

  @Column(updatable = false) // 최초 생성 시 설정, 이후 변경 방지
  private ZonedDateTime createdAt;

  private ZonedDateTime updatedAt;

  private ZonedDateTime deletedAt;

  private boolean isOnCreate() {
    return id == null;
  }

  private void onCreate() {
    id = UUID.randomUUID(); // UUID 자동 생성
    createdAt = ZonedDateTime.now();
    updatedAt = createdAt;
  }

  private void onUpdate() {
    updatedAt = ZonedDateTime.now();
  }

  private boolean isOnDelete() {
    return isDeleted != null && isDeleted;
  }

  public void markDelete() {
    isDeleted = true;
    deletedAt = ZonedDateTime.now();
  }

  public void unmarkDelete() {
    isDeleted = false;
    deletedAt = null;
  }

  @PrePersist
  public void prePersist() {
    if (isOnCreate()) {
      onCreate();
    }
    if (isOnDelete()) { // isDeleted 기본값 보장
      markDelete();
    } else {
      unmarkDelete();
    }
  }

  @PreUpdate
  public void preUpdate() {
    onUpdate();
    if (isOnDelete()) {
      markDelete();
    } else {
      unmarkDelete();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    BaseEntity entity = (BaseEntity) o;
    return id != null && id.equals(entity.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
