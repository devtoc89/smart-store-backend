package com.smartstore.api.v1.application.admin.categoryl1;

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

import com.smartstore.api.v1.application.admin._config.constants.BaseURLConstants;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1FilterRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1PatchRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1PostRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1PutRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1ResponseDTO;
import com.smartstore.api.v1.application.admin.categoryl1.service.AdminCategoryL1AppService;
import com.smartstore.api.v1.common.domain.dto.CustomErrorResponseDTO;
import com.smartstore.api.v1.common.dto.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

class Config {
  protected static final String BASE_URL = BaseURLConstants.BASE_URL + "/categories/l1";

  private Config() {
    throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
  }
}

@Validated
@RestController
@RequestMapping(Config.BASE_URL)
@Tag(name = "Admin Category L1 API", description = "1차 카테고리 관리 API")
public class AdminCategoryL1Controller {

  private final AdminCategoryL1AppService adminCategoryL1Facade;

  public AdminCategoryL1Controller(AdminCategoryL1AppService adminCategoryL1Facade) {
    this.adminCategoryL1Facade = adminCategoryL1Facade;
  }

  @GetMapping("")
  @Operation(summary = "1차 카테고리 목록 조회", description = "필터를 적용하여 1차 카테고리 목록을 페이지네이션하여 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<AdminCategoryL1ResponseDTO>>> searchCategoryL1s(
      @ModelAttribute @Valid AdminCategoryL1FilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<AdminCategoryL1ResponseDTO> result = adminCategoryL1Facade.getCategoryL1sByFilterWithPaging(searchRequest,
        pageable);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PostMapping("")
  @Operation(summary = "1차 카테고리 생성", description = "새로운 1차 카테고리를 생성합니다.")
  @ApiResponse(responseCode = "201", description = "생성됨", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL1ResponseDTO>> postCategoryL1(
      @RequestBody @Valid AdminCategoryL1PostRequestDTO requestDTO) {
    AdminCategoryL1ResponseDTO result = adminCategoryL1Facade.postCategoryL1(requestDTO);
    URI location = URI.create(Config.BASE_URL + "/" + result.getId());
    return ResponseEntity.created(location).body(ResponseWrapper.success(result));
  }

  @GetMapping("/{id}")
  @Operation(summary = "1차 카테고리 상세 조회", description = "ID를 기반으로 특정 1차 카테고리 정보를 조회합니다.")
  @Parameter(name = "id", description = "UUID 형식의 1차 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL1ResponseDTO>> getCategoryL1(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    AdminCategoryL1ResponseDTO result = adminCategoryL1Facade.getCategoryL1ById(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PutMapping("/{id}")
  @Operation(summary = "1차 카테고리 수정", description = "ID를 기반으로 1차 카테고리 정보를 수정합니다.")
  @Parameter(name = "id", description = "UUID 형식의 1차 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL1ResponseDTO>> putCategoryL1(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminCategoryL1PutRequestDTO requestDTO) {
    AdminCategoryL1ResponseDTO result = adminCategoryL1Facade.putCategoryL1(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "1차 카테고리 일부 수정", description = "ID를 기반으로 특정 필드만 업데이트합니다.")
  @Parameter(name = "id", description = "UUID 형식의 1차 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL1ResponseDTO>> patchCategoryL1(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminCategoryL1PatchRequestDTO requestDTO) {
    AdminCategoryL1ResponseDTO result = adminCategoryL1Facade.patchCategoryL1(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "1차 카테고리 삭제", description = "ID를 기반으로 1차 카테고리를 삭제합니다. (논리 삭제)")
  @Parameter(name = "id", description = "UUID 형식의 1차 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryL1ResponseDTO>> deleteCategoryL1(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    AdminCategoryL1ResponseDTO result = adminCategoryL1Facade.deleteCategoryL1(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }
}
