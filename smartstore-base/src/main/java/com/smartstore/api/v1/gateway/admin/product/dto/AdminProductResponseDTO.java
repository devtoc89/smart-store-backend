package com.smartstore.api.v1.gateway.admin.product.dto;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.utils.DateUtils;
import com.smartstore.api.v1.domain.product.vo.ProductVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "상품 응답 DTO")
public class AdminProductResponseDTO {

  @Schema(description = "말단 카테고리 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID categoryId;

  @Schema(description = "상품 이름", example = "사과")
  private String name;

  @Schema(description = "상품 가격", example = "1000")
  private Integer price;

  @Schema(description = "상품 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "상품 생성 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-15 14:30:00")
  private String createdAt;

  @Schema(description = "상품 수정 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-16 10:00:00")
  private String updatedAt;

  @Schema(description = "상품 삭제 일자 (yyyy-MM-dd HH:mm:ss)", example = "2025-03-20 18:45:00")
  private String deletedAt;

  @Schema(description = "상품 삭제 여부", example = "false")
  private Boolean isDeleted;

  public AdminProductResponseDTO(ProductVO product) {
    this.name = product.getName();
    this.price = product.getPrice();
    this.categoryId = product.getCategoryId();
    this.id = product.getBase().getId();
    this.createdAt = DateUtils.formatWithDefaultZone(product.getBase().getCreatedAt());
    this.updatedAt = DateUtils.formatWithDefaultZone(product.getBase().getUpdatedAt());
    this.deletedAt = DateUtils.formatWithDefaultZone(product.getBase().getDeletedAt());
    this.isDeleted = product.getBase().getIsDeleted();
  }

  public static Page<AdminProductResponseDTO> fromVOWithPage(Page<ProductVO> products) {
    // TODO: List의 경우 overfetch 고려하여 요소 제한을 고려해야함
    return products.map(AdminProductResponseDTO::new);
  }

  public static AdminProductResponseDTO fromVO(ProductVO product) {
    return product == null ? null : new AdminProductResponseDTO(product);
  }
}
