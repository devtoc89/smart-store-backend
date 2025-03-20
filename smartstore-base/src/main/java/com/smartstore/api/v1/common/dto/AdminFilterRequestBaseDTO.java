package com.smartstore.api.v1.common.dto;

import java.util.List;
import java.util.Objects;

import com.smartstore.api.v1.common.domain.vo.BaseFilterConditionVO;
import com.smartstore.api.v1.common.utils.DateUtils;
import com.smartstore.api.v1.common.utils.StringUtils;
import com.smartstore.api.v1.common.validation.date.ValidDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
public class AdminFilterRequestBaseDTO {
  @Schema(description = "조회할 ID 목록", example = "[\"550e8400-e29b-41d4-a716-446655440000\", \"660e8400-e29b-41d4-a716-446655440001\"]")
  private List<String> id;

  @Schema(description = "조회 시작 날짜 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-01 00:00:00")
  @ValidDateTime(message = "시작 날짜 형식이 올바르지 않습니다. (yyyy-MM-dd HH:mm:ss)")
  private String from;

  @Schema(description = "조회 종료 날짜 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-15 23:59:59")
  @ValidDateTime(message = "종료 날짜 형식이 올바르지 않습니다. (yyyy-MM-dd HH:mm:ss)")
  private String to;

  @Schema(description = "삭제 여부 필터 (true: 삭제된 항목 포함, false: 삭제되지 않은 항목만)", example = "false")
  private Boolean isDeleted;

  public BaseFilterConditionVO toBaseFilterConditionVO() {
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

}
