package com.smartstore.api.v1.common.domain.query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.smartstore.api.v1.common.domain.entity.BaseEntity;

public class BaseSpecification {

  private BaseSpecification() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static <T extends BaseEntity> Specification<T> inId(List<UUID> ids) {
    return (root, query, criteriaBuilder) -> (ids == null || ids.isEmpty()) ? criteriaBuilder.conjunction()
        : root.get("id").in(ids);
  }

  public static <T extends BaseEntity> Specification<T> isCreatedAfter(ZonedDateTime fromDate) {
    return (root, query, criteriaBuilder) -> (fromDate == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
  }

  public static <T extends BaseEntity> Specification<T> isCreatedBefore(ZonedDateTime toDate) {
    return (root, query, criteriaBuilder) -> (toDate == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.lessThan(root.get("createdAt"), toDate);
  }

  public static <T extends BaseEntity> Specification<T> isDeleted(Boolean isDeleted) {
    return (root, query, criteriaBuilder) -> (isDeleted == null) ? criteriaBuilder.conjunction()
        : criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
  }
}
