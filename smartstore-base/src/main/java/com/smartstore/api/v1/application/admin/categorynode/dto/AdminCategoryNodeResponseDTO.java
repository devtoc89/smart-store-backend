package com.smartstore.api.v1.application.admin.categorynode.dto;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.dto.AdminResponseBaseDTO;
import com.smartstore.api.v1.domain.category.vo.CategoryNodeVO;

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
@Schema(description = "말단 카테고리 응답 DTO")
public class AdminCategoryNodeResponseDTO extends AdminResponseBaseDTO {

  @Schema(description = "2차 카테고리 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
  private String categoryL2Id;

  @Schema(description = "말단 카테고리 이름", example = "전자제품")
  private String name;

  @Schema(description = "2차 카테고리 정렬 순서서", example = "2")
  private Integer orderBy;

  public AdminCategoryNodeResponseDTO(CategoryNodeVO vo) {
    super(vo.getBase());
    this.categoryL2Id = vo.getCategoryL2Id().toString();
    this.name = vo.getName();
    this.orderBy = vo.getOrderBy();
  }

  public static Page<AdminCategoryNodeResponseDTO> fromVOWithPage(Page<CategoryNodeVO> voList) {
    // TODO: List의 경우 overfetch 고려하여 요소 제한을 고려해야함
    return voList.map(AdminCategoryNodeResponseDTO::new);
  }

  public static AdminCategoryNodeResponseDTO fromVO(CategoryNodeVO vo) {
    return vo == null ? null : new AdminCategoryNodeResponseDTO(vo);
  }
}
