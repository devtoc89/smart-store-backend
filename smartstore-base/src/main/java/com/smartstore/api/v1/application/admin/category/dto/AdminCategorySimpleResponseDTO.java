package com.smartstore.api.v1.application.admin.category.dto;

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
public class AdminCategorySimpleResponseDTO extends AdminResponseBaseDTO {

  @Schema(description = "카테고리 이름", example = "전자제품")
  private String name;

  @Schema(description = "카테고리 정렬 순서", example = "2")
  private Integer orderBy;

  public AdminCategorySimpleResponseDTO(CategoryVO vo) {
    super(vo.getBase());
    this.name = vo.getName();
    this.orderBy = vo.getOrderBy();
  }

  public static Page<AdminCategoryResponseDTO> fromVOWithPage(Page<CategoryVO> voList) {
    return voList.map(AdminCategoryResponseDTO::new);
  }

  public static AdminCategoryResponseDTO fromVO(CategoryVO vo) {
    return vo == null ? null : new AdminCategoryResponseDTO(vo);
  }
}
