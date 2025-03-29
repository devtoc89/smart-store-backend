package com.smartstore.api.v1.application.admin.superadmin.dto;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.dto.AdminResponseBaseDTO;
import com.smartstore.api.v1.common.utils.date.DateUtil;
import com.smartstore.api.v1.domain.admin.vo.AdminVO;

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
@Schema(description = "상품 응답 DTO")
public class SuperAdminResponseDTO extends AdminResponseBaseDTO {

  @Schema(description = "상품 이름", example = "사과")
  private String email;

  @Schema(description = "상품 이름", example = "사과")
  private String nickname;

  @Schema(description = "상품 가격", example = "1000")
  private Boolean isActivated;

  @Schema(description = "상품 가격", example = "1000")
  private Integer loginFailCount;

  @Schema(description = "상품 가격", example = "1000")
  private String lastLoginAt;

  public SuperAdminResponseDTO(AdminVO vo) {
    super(vo.getBase());
    this.email = vo.getEmail();
    this.nickname = vo.getNickname();
    this.isActivated = vo.getIsActivated();
    this.loginFailCount = vo.getLoginFailCount();
    this.lastLoginAt = Optional.ofNullable(vo.getLastLoginAt()).map(DateUtil::formatWithDefaultZone).orElse("");

  }

  public static Page<SuperAdminResponseDTO> fromVOWithPage(Page<AdminVO> vos) {
    // TODO: List의 경우 overfetch 고려하여 요소 제한을 고려해야함
    return vos.map(SuperAdminResponseDTO::new);
  }

  public static SuperAdminResponseDTO fromVO(AdminVO vo) {
    return Optional.ofNullable(vo).map(SuperAdminResponseDTO::new).orElse(null);
  }
}
