package com.smartstore.api.v1.application.admin.categorynode.dto;

import com.smartstore.api.v1.domain.category.vo.CategoryNodeVO;

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
@Schema(description = "말단 카테고리 트리 응답 DTO")
public class AdminCategoryNodeTreeResponseDTO extends AdminCategoryNodeResponseDTO {

  public AdminCategoryNodeTreeResponseDTO(CategoryNodeVO vo) {
    super(vo);
  }
}