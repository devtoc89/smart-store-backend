package com.smartstore.api.v1.domain.admin.vo;

import org.springframework.data.jpa.domain.Specification;

import com.smartstore.api.v1.common.domain.vo.BaseFilterConditionVO;
import com.smartstore.api.v1.domain.admin.entity.Admin;
import com.smartstore.api.v1.domain.admin.query.AdminSpecification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AdminFilterConditionVO {
  private final BaseFilterConditionVO base;
  private final String nickname;
  private final String email;

  public Specification<Admin> toSpecification() {

    Specification<Admin> cond = AdminSpecification.hasEmail(email)
        .and(AdminSpecification.hasNickname(nickname));

    if (base != null) {
      cond = cond.and(base.<Admin>toSubSpecification());
    }

    return Specification.where(cond);
  }
}
