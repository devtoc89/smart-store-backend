package com.smartstore.api.v1.application.admin.category;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryFilterRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryPatchRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryPostRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryPutRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.AdminCategoryResponseDTO;
import com.smartstore.api.v1.application.admin.category.dto.tree.AdminCategoryL1TreePutRequestDTO;
import com.smartstore.api.v1.application.admin.category.dto.tree.AdminCategoryL1TreeResponseDTO;
import com.smartstore.api.v1.application.admin.category.service.AdminCategoryAppService;
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

class Config {
  protected static final String BASE_URL = AdminBaseURLConstants.BASE_URL + "/categories";

  private Config() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(Config.BASE_URL)
@Tag(name = "Admin Category API", description = "카테고리 관리 API")
public class AdminCategoryController {

  private final AdminCategoryAppService adminCategoryAppService;

  @GetMapping("")
  @Operation(summary = "카테고리 전체 조회", description = "필터를 적용하여 카테고리 목록을 페이지네이션하여 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<List<AdminCategoryL1TreeResponseDTO>>> search() {
    var result = adminCategoryAppService.getTreeList();
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PostMapping("")
  @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
  @ApiResponse(responseCode = "201", description = "생성됨", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryResponseDTO>> post(
      @RequestBody @Valid AdminCategoryPostRequestDTO requestDTO) throws BindException {

    var result = adminCategoryAppService.post(requestDTO);
    var location = URI.create(Config.BASE_URL + "/l" + result.getLevel() + "/" + result.getId());
    return ResponseEntity.created(location).body(ResponseWrapper.success(result));
  }

  @PutMapping("")
  @Operation(summary = "카테고리 전체 수정", description = "카테고리 전체 정보를 수정합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<String>> putAll(
      @RequestBody @Valid List<AdminCategoryL1TreePutRequestDTO> requestDTO) {
    adminCategoryAppService.putAll(requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success("카테고리 전체 수정이 완료되었습니다."));
  }

  @GetMapping("/l1")
  @Operation(summary = "1차 카테고리 목록 조회", description = "필터를 적용하여 1차 카테고리 목록을 페이지네이션하여 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<AdminCategoryResponseDTO>>> searchL1(
      @ModelAttribute @Valid AdminCategoryFilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "orderBy", direction = Sort.Direction.ASC) Pageable pageable) {
    var result = adminCategoryAppService.getList(searchRequest,
        pageable, 1);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @GetMapping("/l2")
  @Operation(summary = "2차 카테고리 목록 조회", description = "필터를 적용하여 2차 카테고리 목록을 페이지네이션하여 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<AdminCategoryResponseDTO>>> searchL2(
      @ModelAttribute @Valid AdminCategoryFilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "orderBy", direction = Sort.Direction.ASC) Pageable pageable) {
    var result = adminCategoryAppService.getList(searchRequest,
        pageable, 2);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @GetMapping("/l3")
  @Operation(summary = "3차 카테고리 목록 조회", description = "필터를 적용하여 3차 카테고리 목록을 페이지네이션하여 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<AdminCategoryResponseDTO>>> searchL3(
      @ModelAttribute @Valid AdminCategoryFilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "orderBy", direction = Sort.Direction.ASC) Pageable pageable) {
    var result = adminCategoryAppService.getList(searchRequest,
        pageable, 3);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @GetMapping("/{id}")
  @Operation(summary = "카테고리 상세 조회", description = "ID를 기반으로 특정 카테고리 정보를 조회합니다.")
  @Parameter(name = "id", description = "UUID 형식의 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryResponseDTO>> get(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    var result = adminCategoryAppService.get(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PutMapping("/{id}")
  @Operation(summary = "카테고리 수정", description = "ID를 기반으로 카테고리 정보를 수정합니다.")
  @Parameter(name = "id", description = "UUID 형식의 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryResponseDTO>> put(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminCategoryPutRequestDTO requestDTO) throws BindException {
    var result = adminCategoryAppService.put(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "카테고리 일부 수정", description = "ID를 기반으로 특정 필드만 업데이트합니다.")
  @Parameter(name = "id", description = "UUID 형식의 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryResponseDTO>> patch(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id,
      @RequestBody @Valid AdminCategoryPatchRequestDTO requestDTO) throws BindException {
    var result = adminCategoryAppService.patch(id, requestDTO);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "카테고리 삭제", description = "ID를 기반으로 카테고리를 삭제합니다. (논리 삭제)")
  @Parameter(name = "id", description = "UUID 형식의 카테고리 ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<AdminCategoryResponseDTO>> delete(
      @PathVariable("id") @NotBlank(message = "id는 필수항목입니다.") @UUID(message = "id는 UUID 형식이어야 합니다.") String id) {
    var result = adminCategoryAppService.delete(id);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }
}
