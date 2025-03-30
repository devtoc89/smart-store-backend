package com.smartstore.api.v1.application.admin.categoryl2.dto;

import java.util.List;

import com.smartstore.api.v1.application.admin.categorynode.dto.AdminCategoryNodeResponseDTO;
import com.smartstore.api.v1.domain.category.vo.CategoryL2VO;

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
public class AdminCategoryL2TreeResponseDTO extends AdminCategoryL2ResponseDTO {

  @Schema(description = "말단 카테고리 리스트")
  private List<AdminCategoryNodeResponseDTO> children;

  public AdminCategoryL2TreeResponseDTO(CategoryL2VO vo, List<AdminCategoryNodeResponseDTO> children) {
    super(vo);
    this.children = children;
  }
}