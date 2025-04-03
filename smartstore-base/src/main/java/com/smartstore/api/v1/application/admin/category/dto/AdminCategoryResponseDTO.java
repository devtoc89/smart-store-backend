package com.smartstore.api.v1.application.admin.category.dto;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.dto.AdminResponseBaseDTO;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "카테고리 응답 DTO")
public class AdminCategoryResponseDTO extends AdminResponseBaseDTO {

  @Schema(description = "카테고리 이름", example = "전자제품")
  private String name;

  @Schema(description = "카테고리 정렬 순서", example = "2")
  private Integer orderBy;

  @Schema(description = "카테고리 레벨", example = "2")
  private Integer level;

  @Schema(description = "부모 카테고리 id", example = "550e8400-e29b-41d4-a716-446655440000")
  private String parentId;

  public AdminCategoryResponseDTO(CategoryVO vo) {
    super(vo.getBase());
    this.name = vo.getName();
    this.orderBy = vo.getOrderBy();
    this.level = vo.getLevel();
    this.parentId = Optional.ofNullable(vo.getParentId()).map(UUID::toString).orElse(null);
  }

  public static Page<AdminCategoryResponseDTO> fromVOWithPage(Page<CategoryVO> voList) {
    return voList.map(AdminCategoryResponseDTO::new);
  }

  public static AdminCategoryResponseDTO fromVO(CategoryVO vo) {
    return vo == null ? null : new AdminCategoryResponseDTO(vo);
  }
}
