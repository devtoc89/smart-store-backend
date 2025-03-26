package com.smartstore.api.v1.application.admin.product;

import java.net.URI;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartstore.api.v1.application.admin.product.dto.AdminProductFilterRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPatchRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPostRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductPutRequestDTO;
import com.smartstore.api.v1.application.admin.product.dto.AdminProductResponseDTO;
import com.smartstore.api.v1.application.admin.product.service.AdminProductAppService;
import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.constants.url.AdminBaseURLConstants;
import com.smartstore.api.v1.common.dto.CustomErrorResponseDTO;
import com.smartstore.api.v1.common.dto.ResponseWrapper;
import com.smartstore.api.v1.domain.storedfile.service.StoredFileService;
import com.smartstore.api.v1.domain.storedfile.vo.StoredPresignedFileDTO;

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
  protected static final String BASE_URL = AdminBaseURLConstants.BASE_URL + "/products";

  protected static final String TAG_NAME = "Admin Product API";
  protected static final String TAG_DESC = "상품 관리 API";

  protected static final String FILE_DOMAIN = "PRODUCT";

  private Config() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(Config.BASE_URL)
@Tag(name = Config.TAG_NAME, description = Config.TAG_DESC)
public class AdminProductController {

  private final AdminProductAppService adminProductAppService;
  private final StoredFileService fileService;

  @GetMapping("")
  @Operation(summary = "상품 목록 조회", description = "상품을 필터링하여 페이지네이션된 목록을 반환합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<AdminProductResponseDTO>>> searchProducts(
      @ModelAttribute @Valid AdminProductFilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<AdminProductResponseDTO> result = adminProductAppService.getList(searchRequest, pageable);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PostMapping("")
  @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
  @ApiResponse(responseCode = "201", description = "생성됨", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> postProduct(
      @RequestBody @Valid AdminProductPostRequestDTO requestDTO) throws BindException {
    AdminProductResponseDTO result = adminProductAppService.post(requestDTO);
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
    AdminProductResponseDTO result = adminProductAppService.get(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PutMapping("/{id}")
  @Operation(summary = "상품 수정", description = "ID를 기반으로 상품을 수정합니다.")
  @Parameter(name = "id", description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> putProduct(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminProductPutRequestDTO requestDTO) throws BindException {
    AdminProductResponseDTO result = adminProductAppService.put(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "상품 일부 수정", description = "ID를 기반으로 특정 상품 필드를 업데이트합니다.")
  @Parameter(name = "id", description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> patchProduct(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminProductPatchRequestDTO requestDTO) throws BindException {
    AdminProductResponseDTO result = adminProductAppService.patch(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "상품 삭제", description = "ID를 기반으로 상품을 삭제합니다. (논리 삭제)")
  @Parameter(name = "id", description = "UUID 형식의 상품 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminProductResponseDTO>> deleteProduct(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    AdminProductResponseDTO result = adminProductAppService.delete(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  // TODO: 사이즈별 이미지
  @GetMapping("/pre-signed-url")
  @Operation(summary = "상품 이미지 등록 pre-signed-url 생성", description = "이미지를 등록하기 위한 URL을 생성합니다.")
  @Parameter(name = "filename", description = "파일명", required = true, example = "사과 이미지.png")
  @Parameter(name = "size", description = "파일 사이즈(byte)", required = true, example = "4627")
  public ResponseEntity<ResponseWrapper<StoredPresignedFileDTO>> generateUrl(
      @RequestParam(name = "filename", required = false, defaultValue = "unnamed") String filename,
      @RequestParam(name = "size", required = false, defaultValue = "0") Long size) {

    // TODO: filename, fileextension, domain validation
    var dto = fileService.generatePreSignedUrl(filename, Config.FILE_DOMAIN, size);
    return ResponseEntity.ok(ResponseWrapper.success(dto));
  }
}
