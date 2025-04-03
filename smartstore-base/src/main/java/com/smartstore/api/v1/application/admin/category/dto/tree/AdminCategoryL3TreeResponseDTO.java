package com.smartstore.api.v1.application.admin.category.dto.tree;

import com.smartstore.api.v1.application.admin.category.dto.AdminCategorySimpleResponseDTO;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Schema(description = "3차 카테고리 트리 응답 DTO")
public class AdminCategoryL3TreeResponseDTO extends AdminCategorySimpleResponseDTO {

  public AdminCategoryL3TreeResponseDTO(CategoryVO vo) {
    super(vo);
  }
}