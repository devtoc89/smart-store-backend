package com.smartstore.api.v1.application.admin.category.dto.tree;

import java.util.List;

import com.smartstore.api.v1.application.admin.category.dto.AdminCategorySimpleResponseDTO;
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
@Schema(description = "2차 카테고리 트리 응답 DTO")
public class AdminCategoryL2TreeResponseDTO extends AdminCategorySimpleResponseDTO {

  @Schema(description = "3차 카테고리 리스트")
  private List<AdminCategoryL3TreeResponseDTO> children;

  public AdminCategoryL2TreeResponseDTO(CategoryVO vo, List<AdminCategoryL3TreeResponseDTO> children) {
    super(vo);
    this.children = children;
  }
}