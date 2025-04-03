package com.smartstore.api.v1.application.admin.category.dto.tree;

import java.util.List;

import com.smartstore.api.v1.application.admin.category.dto.AdminCategorySimpleResponseDTO;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;

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
@Schema(description = "1차 카테고리 트리 응답 DTO")
public class AdminCategoryL1TreeResponseDTO extends AdminCategorySimpleResponseDTO {

  @Schema(description = "2차 카테고리 리스트")
  private List<AdminCategoryL2TreeResponseDTO> children;

  public AdminCategoryL1TreeResponseDTO(CategoryVO vo, List<AdminCategoryL2TreeResponseDTO> children) {
    super(vo);
    this.children = children;
  }

  public static List<AdminCategoryL1TreeResponseDTO> fromVO(CategoryNestVO vo) {
    return vo.getCategoryL1Nest().stream()
        .map(l1 -> new AdminCategoryL1TreeResponseDTO(l1.getCategory(), l1.getChildren().stream()
            .map(l2 -> new AdminCategoryL2TreeResponseDTO(l2.getCategory(), l2.getChildren().stream()
                .map(node -> new AdminCategoryL3TreeResponseDTO(node.getCategory()))
                .toList()))
            .toList()))
        .toList();
  }
}
