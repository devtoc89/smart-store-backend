package com.smartstore.api.v1.application.admin.categoryl1.dto;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.dto.AdminResponseBaseDTO;
import com.smartstore.api.v1.domain.category.vo.CategoryL1VO;

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
@Schema(description = "1차 카테고리 응답 DTO")
public class AdminCategoryL1ResponseDTO extends AdminResponseBaseDTO {

  @Schema(description = "1차 카테고리 이름", example = "전자제품")
  private String name;

  public AdminCategoryL1ResponseDTO(CategoryL1VO vo) {
    super(vo.getBase());
    this.name = vo.getName();
  }

  public static Page<AdminCategoryL1ResponseDTO> fromVOWithPage(Page<CategoryL1VO> voList) {
    // TODO: List의 경우 overfetch 고려하여 요소 제한을 고려해야함
    return voList.map(AdminCategoryL1ResponseDTO::new);
  }

  public static AdminCategoryL1ResponseDTO fromVO(CategoryL1VO vo) {
    return vo == null ? null : new AdminCategoryL1ResponseDTO(vo);
  }
}
