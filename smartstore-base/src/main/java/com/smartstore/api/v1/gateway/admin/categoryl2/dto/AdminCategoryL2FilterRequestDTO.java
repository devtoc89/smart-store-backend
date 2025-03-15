package com.smartstore.api.v1.gateway.admin.categoryl2.dto;

import java.util.List;
import java.util.Objects;

import com.smartstore.api.v1.common.domain.vo.BaseFilterConditionVO;
import com.smartstore.api.v1.common.utils.DateUtils;
import com.smartstore.api.v1.common.utils.StringUtils;
import com.smartstore.api.v1.common.validation.date.ValidDateTime;
import com.smartstore.api.v1.domain.category.vo.CategoryL2FilterConditionVO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminCategoryL2FilterRequestDTO {

  @Schema(description = "조회할 카테고리 ID 목록", example = "[\"550e8400-e29b-41d4-a716-446655440000\", \"660e8400-e29b-41d4-a716-446655440001\"]")
  private List<String> id;

  @Schema(description = "검색어 (2글자 이상 입력)", example = "전자제품")
  @Pattern(regexp = "^$|.{2,}", message = "검색어는 2글자 이상 입력해야 합니다.")
  private String keyword;

  @Schema(description = "조회 시작 날짜 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-01 00:00:00")
  @ValidDateTime(message = "시작 날짜 형식이 올바르지 않습니다. (yyyy-MM-dd HH:mm:ss)")
  private String from;

  @Schema(description = "조회 종료 날짜 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-15 23:59:59")
  @ValidDateTime(message = "종료 날짜 형식이 올바르지 않습니다. (yyyy-MM-dd HH:mm:ss)")
  private String to;

  @Schema(description = "삭제 여부 필터 (true: 삭제된 항목 포함, false: 삭제되지 않은 항목만)", example = "false")
  private Boolean isDeleted;

  private BaseFilterConditionVO toBaseFilterConditionVO() {
    return BaseFilterConditionVO.builder()
        .fromDate(DateUtils.parseWithDefaultZone(from))
        .toDate(DateUtils.parseWithDefaultZone(to))
        .isDeleted(isDeleted)
        .ids(id == null ? null
            : id.stream()
                .map(StringUtils::stringToUUID)
                .filter(Objects::nonNull)
                .toList())
        .build();
  }

  public CategoryL2FilterConditionVO toSearchConditionVO() {
    return CategoryL2FilterConditionVO.builder()
        .base(toBaseFilterConditionVO())
        .name(keyword)
        .build();
  }
}
