package com.smartstore.api.v1.application.admin.productimage.dto;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.common.dto.AdminResponseBaseDTO;
import com.smartstore.api.v1.domain.product.vo.ProductImageVO;

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
public class AdminProductImageResponseDTO extends AdminResponseBaseDTO {

  @Schema(description = "상품 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
  private String productId;

  @Schema(description = "이미지 주소", example = "http://demo.cloudfont.com/abc")
  private String url;

  @Schema(description = "이미지 이름", example = "LG 컴퓨터 사진1")
  private String name;

  @Schema(description = "컨텐츠 타입", example = "image/png")
  private String contentType;

  @Schema(description = "이미지 사이즈", example = "1000")
  private Long size;

  public AdminProductImageResponseDTO(ProductImageVO vo, String cdnUrl) {
    super(vo.getBase());
    this.productId = vo.getProductId().toString();
    this.url = Optional.ofNullable(cdnUrl).orElse("") + vo.getFile().getKey();
    this.name = vo.getFile().getFilename();
    this.contentType = vo.getFile().getContentType();
    this.size = vo.getFile().getFileSize();
  }

  public static Page<AdminProductImageResponseDTO> fromVOWithPage(Page<ProductImageVO> voList, String cdnUrl) {
    // TODO: List의 경우 overfetch 고려하여 요소 제한을 고려해야함
    return voList.map(v -> fromVO(v, cdnUrl));
  }

  public static AdminProductImageResponseDTO fromVO(ProductImageVO vo, String cdnUrl) {
    return vo == null ? null : new AdminProductImageResponseDTO(vo, cdnUrl);
  }
}
