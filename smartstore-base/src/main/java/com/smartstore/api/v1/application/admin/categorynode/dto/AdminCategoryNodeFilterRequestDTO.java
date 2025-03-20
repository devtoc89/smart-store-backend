package com.smartstore.api.v1.application.admin.categorynode.dto;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.common.dto.AdminFilterRequestBaseDTO;
import com.smartstore.api.v1.common.utils.StringUtils;
import com.smartstore.api.v1.domain.category.vo.CategoryNodeFilterConditionVO;

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
@Schema(description = "카테고리(말단) 조건 조회 DTO")
public class AdminCategoryNodeFilterRequestDTO extends AdminFilterRequestBaseDTO {

  @Schema(description = "검색어 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String keyword;

  @UUID
  @Schema(description = "카테고리(중) ID", example = "\"550e8400-e29b-41d4-a716-446655440000\"")
  private String categoryL2Id;

  public CategoryNodeFilterConditionVO toSearchConditionVO() {
    return CategoryNodeFilterConditionVO.builder()
        .base(toBaseFilterConditionVO())
        .categoryL2Id(StringUtils.stringToUUID(categoryL2Id))
        .name(keyword)
        .build();
  }
}
