package com.smartstore.api.v1.gateway.admin.categoryl2.dto;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.utils.DateUtils;
import com.smartstore.api.v1.domain.category.vo.CategoryL2VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "2차 카테고리 응답 DTO")
public class AdminCategoryL2ResponseDTO {

  @Schema(description = "1차 카테고리 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
  private String categoryL1Id;

  @Schema(description = "2차 카테고리 이름", example = "전자제품")
  private String name;

  @Schema(description = "2차 카테고리 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "카테고리 생성 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-15 14:30:00")
  private String createdAt;

  @Schema(description = "카테고리 수정 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-16 10:00:00")
  private String updatedAt;

  @Schema(description = "카테고리 삭제 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-20 18:45:00")
  private String deletedAt;

  @Schema(description = "카테고리 삭제 여부", example = "false")
  private Boolean isDeleted;

  public AdminCategoryL2ResponseDTO(CategoryL2VO vo) {
    this.categoryL1Id = vo.getCategoryL1Id().toString();
    this.name = vo.getName();

    this.id = vo.getBase().getId();
    this.createdAt = DateUtils.formatWithDefaultZone(vo.getBase().getCreatedAt());
    this.updatedAt = DateUtils.formatWithDefaultZone(vo.getBase().getUpdatedAt());
    this.deletedAt = DateUtils.formatWithDefaultZone(vo.getBase().getDeletedAt());
    this.isDeleted = vo.getBase().getIsDeleted();
  }

  public static Page<AdminCategoryL2ResponseDTO> fromVOWithPage(Page<CategoryL2VO> voList) {
    // TODO: List의 경우 overfetch 고려하여 요소 제한을 고려해야함
    return voList.map(AdminCategoryL2ResponseDTO::new);
  }

  public static AdminCategoryL2ResponseDTO fromVO(CategoryL2VO vo) {
    return vo == null ? null : new AdminCategoryL2ResponseDTO(vo);
  }
}
