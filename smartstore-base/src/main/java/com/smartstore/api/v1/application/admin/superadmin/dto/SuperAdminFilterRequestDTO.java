package com.smartstore.api.v1.application.admin.superadmin.dto;

import com.smartstore.api.v1.common.dto.AdminFilterRequestBaseDTO;
import com.smartstore.api.v1.domain.admin.vo.AdminFilterConditionVO;

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
@Schema(description = "어드민 조건 조회 DTO")
public class SuperAdminFilterRequestDTO extends AdminFilterRequestBaseDTO {

  @Schema(description = "이메일 검색어 (2글자 이상 입력)", example = "white@gmail.com")
  @Pattern(regexp = "^$|.{2,}", message = "이메일 검색어는 2글자 이상 입력해야 합니다.")
  private String email;

  @Schema(description = "닉네임 검색어 (2글자 이상 입력)", example = "하늘")
  @Pattern(regexp = "^$|.{2,}", message = "닉네임 검색어는 2글자 이상 입력해야 합니다.")
  private String nickname;

  public AdminFilterConditionVO toSearchConditionVO() {
    return AdminFilterConditionVO.builder()
        .base(toBaseFilterConditionVO())
        .email(email)
        .nickname(nickname)
        .build();
  }
}
