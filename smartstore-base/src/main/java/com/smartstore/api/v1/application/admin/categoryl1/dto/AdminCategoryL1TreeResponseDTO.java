package com.smartstore.api.v1.application.admin.categoryl1.dto;

import java.util.List;

import com.smartstore.api.v1.application.admin.categoryl2.dto.AdminCategoryL2TreeResponseDTO;
import com.smartstore.api.v1.domain.category.vo.CategoryL1VO;

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
@Schema(description = "1차 카테고리 트리 응답 DTO")
public class AdminCategoryL1TreeResponseDTO extends AdminCategoryL1ResponseDTO {

  @Schema(description = "2차 카테고리 리스트")
  private List<AdminCategoryL2TreeResponseDTO> children;

  public AdminCategoryL1TreeResponseDTO(CategoryL1VO vo, List<AdminCategoryL2TreeResponseDTO> children) {
    super(vo);
    this.children = children;
  }
}
