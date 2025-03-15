package com.smartstore.api.v1.gateway.admin.categoryl2;

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
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2FilterRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2PatchRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2PostRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2PutRequestDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.dto.AdminCategoryL2ResponseDTO;
import com.smartstore.api.v1.gateway.admin.categoryl2.facade.AdminCategoryL2Facade;
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
  protected static final String BASE_URL = "/api/v1/admin/categories/l2";

  private Config() {
    throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
  }
}

@Validated
@RestController
@RequestMapping(Config.BASE_URL)
@Tag(name = "Admin Category L2 API", description = "2차 카테고리 관리 API")
public class AdminCategoryL2Controller {

  private final AdminCategoryL2Facade adminCategoryL2Facade;

  public AdminCategoryL2Controller(AdminCategoryL2Facade adminCategoryL2Facade) {
    this.adminCategoryL2Facade = adminCategoryL2Facade;
  }

  @GetMapping("")
  @Operation(summary = "2차 카테고리 목록 조회", description = "필터를 적용하여 2차 카테고리 목록을 페이지네이션하여 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<AdminCategoryL2ResponseDTO>>> searchCategoryL2s(
      @ModelAttribute @Valid AdminCategoryL2FilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<AdminCategoryL2ResponseDTO> result = adminCategoryL2Facade.getCategoryL2sByFilterWithPaging(searchRequest,
        pageable);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PostMapping("")
  @Operation(summary = "2차 카테고리 생성", description = "새로운 2차 카테고리를 생성합니다.")
  @ApiResponse(responseCode = "201", description = "생성됨", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL2ResponseDTO>> postCategoryL2(
      @RequestBody @Valid AdminCategoryL2PostRequestDTO requestDTO) {
    AdminCategoryL2ResponseDTO result = adminCategoryL2Facade.postCategoryL2(requestDTO);
    URI location = URI.create(Config.BASE_URL + "/" + result.getId());
    return ResponseEntity.created(location).body(ResponseWrapper.success(result));
  }

  @GetMapping("/{id}")
  @Operation(summary = "2차 카테고리 상세 조회", description = "ID를 기반으로 특정 2차 카테고리 정보를 조회합니다.")
  @Parameter(name = "id", description = "UUID 형식의 2차 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL2ResponseDTO>> getCategoryL2(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    AdminCategoryL2ResponseDTO result = adminCategoryL2Facade.getCategoryL2ById(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PutMapping("/{id}")
  @Operation(summary = "2차 카테고리 수정", description = "ID를 기반으로 2차 카테고리 정보를 수정합니다.")
  @Parameter(name = "id", description = "UUID 형식의 2차 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL2ResponseDTO>> putCategoryL2(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminCategoryL2PutRequestDTO requestDTO) {
    AdminCategoryL2ResponseDTO result = adminCategoryL2Facade.putCategoryL2(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "2차 카테고리 일부 수정", description = "ID를 기반으로 특정 필드만 업데이트합니다.")
  @Parameter(name = "id", description = "UUID 형식의 2차 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL2ResponseDTO>> patchCategoryL2(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminCategoryL2PatchRequestDTO requestDTO) {
    AdminCategoryL2ResponseDTO result = adminCategoryL2Facade.patchCategoryL2(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "2차 카테고리 삭제", description = "ID를 기반으로 2차 카테고리를 삭제합니다. (논리 삭제)")
  @Parameter(name = "id", description = "UUID 형식의 2차 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL2ResponseDTO>> deleteCategoryL2(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    AdminCategoryL2ResponseDTO result = adminCategoryL2Facade.deleteCategoryL2(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }
}
