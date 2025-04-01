package com.smartstore.api.v1.domain.common.vo;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.exception.BadRequestException;
import com.smartstore.api.v1.domain.common.entity.BaseEntity;
import com.smartstore.api.v1.domain.common.query.BaseSpecification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseFilterConditionVO {
  private final List<UUID> ids;
  private final ZonedDateTime fromDate;
  private final ZonedDateTime toDate;
  private final Boolean isDeleted;

  public <T extends BaseEntity> Specification<T> toSubSpecification() {
    if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
      throw new BadRequestException("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
    }

    return BaseSpecification.<T>inId(ids)
        .and(BaseSpecification.isDeleted(isDeleted))
        .and(BaseSpecification.isCreatedAfter(fromDate))
        .and(BaseSpecification.isCreatedBefore(toDate));

  }
}