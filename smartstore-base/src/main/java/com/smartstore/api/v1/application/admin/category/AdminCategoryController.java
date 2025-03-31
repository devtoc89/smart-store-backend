package com.smartstore.api.v1.application.admin.category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartstore.api.v1.application.admin.category.service.AdminCategoryAppService;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1TreePutRequestDTO;
import com.smartstore.api.v1.application.admin.categoryl1.dto.AdminCategoryL1TreeResponseDTO;
import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.constants.url.AdminBaseURLConstants;
import com.smartstore.api.v1.common.dto.CustomErrorResponseDTO;
import com.smartstore.api.v1.common.dto.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    var result = adminCategoryAppService.getList();
    return ResponseEntity.ok(ResponseWrapper.success(result));
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

}
