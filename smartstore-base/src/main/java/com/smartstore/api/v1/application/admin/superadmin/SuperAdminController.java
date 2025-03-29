package com.smartstore.api.v1.application.admin.superadmin;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartstore.api.v1.application.admin.superadmin.dto.SuperAdminFilterRequestDTO;
import com.smartstore.api.v1.application.admin.superadmin.dto.SuperAdminResponseDTO;
import com.smartstore.api.v1.application.admin.superadmin.service.SuperAdminAppService;
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
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

final class Config {
  protected static final String BASE_URL = AdminBaseURLConstants.SUPER_ADMIN_URL;
  protected static final String ADMIN_ID_KEY = "id";
  protected static final String ADMIN_ID_SLOT = "{" + ADMIN_ID_KEY + "}";
  protected static final String ACTIVATE_PATH = "/activate/" + ADMIN_ID_SLOT;

  protected static final String TAG_NAME = "Super Admin API";
  protected static final String TAG_DESC = "Super Admin 기능 API";

  protected static final String ACTIVATE_SUCCESS_MESSAGE = "관리자 계정 활성화에 성공하였습니다.";
  protected static final String ACTIVATE_FAIL_MESSAGE = "관리자 계정 활성화에 실패하였습니다.";

  private Config() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}

@RestController
@RequiredArgsConstructor
@RequestMapping(Config.BASE_URL)
@Tag(name = Config.TAG_NAME, description = Config.TAG_DESC)
public class SuperAdminController {
  private final SuperAdminAppService superAdminAppService;

  @GetMapping("")
  @Operation(summary = "어드민 목록 조회", description = "어드민을 필터링하여 페이지네이션된 목록을 반환합니다.")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = CustomErrorResponseDTO.class)))
  public ResponseEntity<ResponseWrapper<Page<SuperAdminResponseDTO>>> searchProducts(
      @ModelAttribute @Valid SuperAdminFilterRequestDTO searchRequest,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<SuperAdminResponseDTO> result = superAdminAppService.getList(searchRequest, pageable);
    return ResponseEntity.ok(ResponseWrapper.success(result));
  }

  @PostMapping(Config.ACTIVATE_PATH)
  public ResponseEntity<ResponseWrapper<String>> signup(@PathVariable(Config.ADMIN_ID_KEY) @NotBlank @UUID String id) {
    var isOk = superAdminAppService.approveAdmin(id);
    if (isOk) {
      return ResponseEntity.ok().body(ResponseWrapper.success(Config.ACTIVATE_SUCCESS_MESSAGE));
    }
    return ResponseEntity.ok().body(ResponseWrapper.fail(Config.ACTIVATE_FAIL_MESSAGE));
  }
}
