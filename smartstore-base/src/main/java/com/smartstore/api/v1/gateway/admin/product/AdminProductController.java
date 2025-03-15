package com.smartstore.api.v1.gateway.admin.product;

import java.net.URI;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartstore.api.v1.common.domain.dto.CustomErrorResponseDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductFilterRequestDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductPatchRequestDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductPostRequestDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductPutRequestDTO;
import com.smartstore.api.v1.gateway.admin.product.dto.AdminProductResponseDTO;
import com.smartstore.api.v1.gateway.admin.product.facade.AdminProductFacade;
import com.smartstore.api.v1.gateway.common.dto.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

class Config {
  protected static final String BASE_URL = "/api/v1/admin/products";

  private Config() {
    throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
  }
}

@Validated
@RestController
@RequestMapping(Config.BASE_URL)
@Tag(name = "Admin Product API", description = "상품 관리 API")
public class AdminProductController {

  private final AdminProductFacade adminProductFacade;

  public AdminProductController(AdminProductFacade adminProductFacade) {
    this.adminProductFacade = adminProductFacade;
  }

  @GetMapping("")
  @Operation(summary = "상품 목록 조회", description = "상품을 필터링하여 페이지네이션된 목록을 반환합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<AdminProductResponseDTO>>> searchProducts(
      @ModelAttribute @Valid AdminProductFilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<AdminProductResponseDTO> result = adminProductFacade.getProductsByFilterWithPaging(searchRequest, pageable);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PostMapping("")
  @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
  @ApiResponse(responseCode = "201", description = "생성됨", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> postProduct(
      @RequestBody @Valid AdminProductPostRequestDTO requestDTO) {
    AdminProductResponseDTO result = adminProductFacade.postProduct(requestDTO);
    URI location = URI.create(Config.BASE_URL + "/" + result.getId());
    return ResponseEntity.created(location).body(ResponseWrapper.success(result));
  }

  @GetMapping("/{id}")
  @Operation(summary = "상품 상세 조회", description = "ID를 기반으로 특정 상품의 상세 정보를 반환합니다.")
  @Parameter(name = "id", description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> getProduct(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    AdminProductResponseDTO result = adminProductFacade.getProductById(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PutMapping("/{id}")
  @Operation(summary = "상품 수정", description = "ID를 기반으로 상품을 수정합니다.")
  @Parameter(name = "id", description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> putProduct(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminProductPutRequestDTO requestDTO) {
    AdminProductResponseDTO result = adminProductFacade.putProduct(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "상품 일부 수정", description = "ID를 기반으로 특정 필드만 업데이트합니다.")
  @Parameter(name = "id", description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> patchProduct(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminProductPatchRequestDTO requestDTO) {
    AdminProductResponseDTO result = adminProductFacade.patchProduct(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "상품 삭제", description = "ID를 기반으로 상품을 삭제합니다. (논리 삭제)")
  @Parameter(name = "id", description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> deleteProduct(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    AdminProductResponseDTO result = adminProductFacade.deleteProduct(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }
}
