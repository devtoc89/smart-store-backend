package com.smartstore.api.v1.application.admin.category.dto;

import java.util.Optional;

import org.hibernate.validator.constraints.UUID;

import com.smartstore.api.v1.common.dto.AdminFilterRequestBaseDTO;
import com.smartstore.api.v1.common.utils.string.StringUtil;
import com.smartstore.api.v1.domain.category.vo.CategoryFilterConditionVO;

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
@Schema(description = "카테고리 조건 조회 DTO")
public class AdminCategoryFilterRequestDTO extends AdminFilterRequestBaseDTO {

  @Schema(description = "검색어", example = "전자제품")
  private String keyword;

  // @Schema(description = "카테고리 레벨", example = "전자제품")
  // private Integer level;

  @UUID
  @Schema(description = "카테고리 부모 ID", example = "전자제품")
  private String parentId;

  public CategoryFilterConditionVO toSearchConditionVO(Integer level) {
    return CategoryFilterConditionVO.builder()
        .base(toBaseFilterConditionVO())
        .name(keyword)
        .level(level)
        .parentId(Optional.ofNullable(parentId).map(StringUtil::stringToUUID).orElse(null))
        .build();
  }
}
