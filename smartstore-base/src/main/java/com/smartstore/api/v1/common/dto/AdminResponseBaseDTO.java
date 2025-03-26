package com.smartstore.api.v1.common.dto;

import com.smartstore.api.v1.common.domain.vo.BaseEntityVO;
import com.smartstore.api.v1.common.utils.date.DateUtil;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
public class AdminResponseBaseDTO {
  @Schema(description = "ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
  private String id;

  @Schema(description = "생성 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-15 14:30:00")
  private String createdAt;

  @Schema(description = "수정 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-16 10:00:00")
  private String updatedAt;

  @Schema(description = "삭제 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-20 18:45:00")
  private String deletedAt;

  @Schema(description = "삭제 여부", example = "false")
  private Boolean isDeleted;

  protected <T extends BaseEntityVO> AdminResponseBaseDTO(T baseEntity) {
    this.id = baseEntity.getId().toString();
    this.createdAt = DateUtil.formatWithDefaultZone(baseEntity.getCreatedAt());
    this.updatedAt = DateUtil.formatWithDefaultZone(baseEntity.getUpdatedAt());
    this.deletedAt = DateUtil.formatWithDefaultZone(baseEntity.getDeletedAt());
    this.isDeleted = baseEntity.getIsDeleted();
  }
}
