package com.smartstore.api.v1.application.admin.categoryl2.dto;

import com.smartstore.api.v1.common.dto.AdminFilterRequestBaseDTO;
import com.smartstore.api.v1.domain.category.vo.CategoryL2FilterConditionVO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
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
@Schema(description = "2차 카테고리 조건 조회 DTO")
public class AdminCategoryL2FilterRequestDTO extends AdminFilterRequestBaseDTO {

  @Schema(description = "검색어 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String keyword;

  public CategoryL2FilterConditionVO toSearchConditionVO() {
    return CategoryL2FilterConditionVO.builder()
        .base(toBaseFilterConditionVO())
        .name(keyword)
        .build();
  }
}
