package com.smartstore.api.v1.application.admin.productimage;

import java.net.URI;
import java.util.List;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartstore.api.v1.application.admin.productimage.dto.AdminProductImageFilterRequestDTO;
import com.smartstore.api.v1.application.admin.productimage.dto.AdminProductImagePostRequestDTO;
import com.smartstore.api.v1.application.admin.productimage.dto.AdminProductImageResponseDTO;
import com.smartstore.api.v1.application.admin.productimage.service.AdminProductImageAppService;
import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.constants.url.AdminBaseURLConstants;
import com.smartstore.api.v1.common.dto.CustomErrorResponseDTO;
import com.smartstore.api.v1.common.dto.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

final class Config {
  protected static final String PRODUCT_ID_KEY = "product_id";
  protected static final String PRODUCT_ID_SLOT = "{" + PRODUCT_ID_KEY + "}";
  protected static final String IMAGE_ID_KEY = "id";
  protected static final String IMAGE_ID_SLOT = "{" + IMAGE_ID_KEY + "}";
  protected static final String PRODUCT_PATH = "/products";
  protected static final String IAMGE_PATH = "/images";
  protected static final String BASE_URL = AdminBaseURLConstants.BASE_URL
      + PRODUCT_PATH + "/" + PRODUCT_ID_SLOT + IAMGE_PATH;

  protected static final String TAG_NAME = "Admin Product image API";
  protected static final String TAG_DESC = "상품 이미지 관리 API";

  private Config() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(Config.BASE_URL)
@Tag(name = Config.TAG_NAME, description = Config.TAG_DESC)
public class AdminProductImageController {

  private final AdminProductImageAppService adminProductImageAppService;

  @GetMapping("")
  @Operation(summary = "상품 이미지 목록 조회", description = "상품 이미지를 필터링하여 페이지네이션된 목록을 반환합니다.")
  @Parameter(name = Config.PRODUCT_ID_KEY, description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<AdminProductImageResponseDTO>>> search(
      @PathVariable(Config.PRODUCT_ID_KEY) @NotBlank(message = Config.PRODUCT_ID_KEY
          + "는 필수항목입니다.") @UUID(message = Config.PRODUCT_ID_KEY + "는 UUID 형식이어야 합니다.") String productId,
      @ModelAttribute @Valid AdminProductImageFilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "orderBy", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<AdminProductImageResponseDTO> result = adminProductImageAppService.getList(productId, searchRequest, pageable);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PostMapping("")
  @Operation(summary = "상품 이미지 생성", description = "새로운 상품 이미지를 생성합니다.")
  @Parameter(name = Config.PRODUCT_ID_KEY, description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "201", description = "생성됨", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductImageResponseDTO>> post(
      @PathVariable(Config.PRODUCT_ID_KEY) @NotBlank(message = Config.PRODUCT_ID_KEY
          + "는 필수항목입니다.") @UUID(message = Config.PRODUCT_ID_KEY + "는 UUID 형식이어야 합니다.") String productId,
      @RequestBody @Valid AdminProductImagePostRequestDTO requestDTO) throws BindException {
    AdminProductImageResponseDTO result = adminProductImageAppService.post(productId, requestDTO);
    URI location = URI.create(AdminBaseURLConstants.BASE_URL
        + Config.PRODUCT_PATH + "/" + productId + Config.IAMGE_PATH + "/" + result.getId());
    return ResponseEntity.created(location).body(ResponseWrapper.success(result));
  }

  @GetMapping("/" + Config.IMAGE_ID_SLOT)
  @Operation(summary = "상품 이미지 상세 조회", description = "ID를 기반으로 특정 상품의 상세 정보를 반환합니다.")
  @Parameter(name = Config.PRODUCT_ID_KEY, description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @Parameter(name = Config.IMAGE_ID_KEY, description = "UUID 형식의 상품 이미지 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductImageResponseDTO>> get(
      @PathVariable(Config.PRODUCT_ID_KEY) @NotBlank(message = Config.PRODUCT_ID_KEY
          + "는 필수항목입니다.") @UUID(message = Config.PRODUCT_ID_KEY + "는 UUID 형식이어야 합니다.") String productId,
      @PathVariable(Config.IMAGE_ID_KEY) @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id)
      throws BindException {
    AdminProductImageResponseDTO result = adminProductImageAppService.get(productId, id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @DeleteMapping("/" + Config.IMAGE_ID_SLOT)
  @Operation(summary = "상품 이미지 삭제", description = "ID를 기반으로 상품을 삭제합니다. (논리 삭제)")
  @Parameter(name = Config.PRODUCT_ID_KEY, description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @Parameter(name = Config.IMAGE_ID_KEY, description = "UUID 형식의 상품 이미지 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductImageResponseDTO>> delete(
      @PathVariable(Config.PRODUCT_ID_KEY) @NotBlank(message = Config.PRODUCT_ID_KEY
          + "는 필수항목입니다.") @UUID(message = Config.PRODUCT_ID_KEY + "는 UUID 형식이어야 합니다.") String productId,
      @PathVariable(Config.IMAGE_ID_KEY) @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id)
      throws BindException {
    AdminProductImageResponseDTO result = adminProductImageAppService.delete(productId, id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PatchMapping("/" + Config.IMAGE_ID_SLOT + "/main")
  @Operation(summary = "상품 대표 이미지 선택", description = "상품 대표 이미지를 지정합니다.")
  @Parameter(name = Config.PRODUCT_ID_KEY, description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @Parameter(name = Config.IMAGE_ID_KEY, description = "UUID 형식의 상품 이미지 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "생성됨", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductImageResponseDTO>> patchMain(
      @PathVariable(Config.PRODUCT_ID_KEY) @NotBlank(message = Config.PRODUCT_ID_KEY
          + "는 필수항목입니다.") @UUID(message = Config.PRODUCT_ID_KEY + "는 UUID 형식이어야 합니다.") String productId,
      @PathVariable(Config.IMAGE_ID_KEY) @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id)
      throws BindException {
    AdminProductImageResponseDTO result = adminProductImageAppService.patchMain(productId, id);
    URI location = URI.create(Config.BASE_URL + "/" + result.getId());
    return ResponseEntity.created(location).body(ResponseWrapper.success(result));
  }

  @PatchMapping("/sort")
  @Parameter(name = Config.PRODUCT_ID_KEY, description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @Operation(summary = "이미지 순서 정렬", description = "이미지 ID 리스트 순서대로 정렬합니다.")
  public ResponseEntity<ResponseWrapper<List<AdminProductImageResponseDTO>>> sortImages(
      @PathVariable(Config.PRODUCT_ID_KEY) @NotBlank @UUID String productId,
      @RequestBody @Valid List<@UUID String> sortedImageIds) throws BindException {
    var res = adminProductImageAppService.reorderImages(productId, sortedImageIds);
    return ResponseEntity.ok(ResponseWrapper.success(res));
  }
}
