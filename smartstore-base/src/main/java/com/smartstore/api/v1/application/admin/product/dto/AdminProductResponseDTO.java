package com.smartstore.api.v1.application.admin.product.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.application.admin.productimage.dto.AdminProductImageResponseDTO;
import com.smartstore.api.v1.common.dto.AdminResponseBaseDTO;
import com.smartstore.api.v1.domain.product.vo.ProductVO;

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
public class AdminProductResponseDTO extends AdminResponseBaseDTO {

  @Schema(description = "말단 카테고리 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
  private String categoryId;

  @Schema(description = "상품 이름", example = "사과")
  private String name;

  @Schema(description = "상품 가격", example = "1000")
  private Integer price;

  @Schema(description = "첨부 이미지 ID 목록", example = "[\"550e8400-e29b-41d4-a716-446655440000\", \"660e8400-e29b-41d4-a716-446655440001\"]")
  private List<AdminProductImageResponseDTO> images;

  public AdminProductResponseDTO(ProductVO product) {
    super(product.getBase());
    this.name = product.getName();
    this.price = product.getPrice();
    this.categoryId = product.getCategoryId().toString();

    this.images = Optional.ofNullable(product.getImages())
        .map(images -> images.stream()
            .map(AdminProductImageResponseDTO::fromVO)
            .toList())
        .orElse(null);
  }

  public static Page<AdminProductResponseDTO> fromVOWithPage(Page<ProductVO> products) {
    // TODO: List의 경우 overfetch 고려하여 요소 제한을 고려해야함
    return products.map(AdminProductResponseDTO::new);
  }

  public static AdminProductResponseDTO fromVO(ProductVO product) {
    return product == null ? null : new AdminProductResponseDTO(product);
  }
}
