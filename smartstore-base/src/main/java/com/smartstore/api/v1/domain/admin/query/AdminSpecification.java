package com.smartstore.api.v1.domain.admin.query;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.utils.sql.SQLUtil;
import com.smartstore.api.v1.domain.admin.entity.Admin;

public class AdminSpecification {
  // Private constructor to prevent instantiation
  private AdminSpecification() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_UTIL_CLASS_MSG);
  }

  public static Specification<Admin> hasEmail(String email) {
    return (root, query, criteriaBuilder) -> email == null ? null
        : criteriaBuilder.like(
            root.get("email"),
            "%" + SQLUtil.escapeLike(email) + "%");
  }

  public static Specification<Admin> hasNickname(String nickname) {
    return (root, query, criteriaBuilder) -> nickname == null ? null
        : criteriaBuilder.like(
            root.get("nickname"),
            "%" + SQLUtil.escapeLike(nickname) + "%");
  }
}
