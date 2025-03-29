package com.smartstore.api.v1.application.admin.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartstore.api.v1.application.admin.admin.dto.AdminLoginRequestDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminLoginResponseDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminSignupRequestDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminTokenRefreshRequestDTO;
import com.smartstore.api.v1.application.admin.admin.dto.AdminTokenRefreshResponseDTO;
import com.smartstore.api.v1.application.admin.admin.service.AdminAuthAppService;
import com.smartstore.api.v1.application.admin.admin.vo.AdminUserDetails;
import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.constants.url.AdminBaseURLConstants;
import com.smartstore.api.v1.common.dto.ResponseWrapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

final class Config {
  protected static final String BASE_URL = AdminBaseURLConstants.BASE_URL + "/users";
  protected static final String SIGNUP_PATH = "/signup";
  protected static final String LOGIN_PATH = "/login";
  protected static final String LOGOUT_PATH = "/logout";
  protected static final String REFRESH_TOKEN_PATH = "/token/refresh";

  protected static final String REGISTER_ADMIN_SUCCESS_MSG = "관리자 계정이 등록되었습니다.";
  protected static final String LOGOUT_SUCCESS_MSG = "로그아웃 되었습니다.";

  protected static final String TAG_NAME = "Admin User API";
  protected static final String TAG_DESC = "유저 관리 API";

  private Config() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}

// TODO: Swagger
// TODO: TEST CASE
@RestController
@RequiredArgsConstructor
@RequestMapping(Config.BASE_URL)
@Tag(name = Config.TAG_NAME, description = Config.TAG_DESC)
public class AdminController {
  private final AdminAuthAppService adminAuthAppService;

  @PostMapping(Config.SIGNUP_PATH)
  public ResponseEntity<ResponseWrapper<String>> signup(@Valid @RequestBody AdminSignupRequestDTO request) {
    adminAuthAppService.registerAdmin(request);
    return ResponseEntity.ok().body(ResponseWrapper.success(Config.REGISTER_ADMIN_SUCCESS_MSG));
  }

  @PostMapping(Config.LOGIN_PATH)
  public ResponseEntity<ResponseWrapper<AdminLoginResponseDTO>> login(
      @Valid @RequestBody AdminLoginRequestDTO request) {
    return ResponseEntity.ok(ResponseWrapper.success(adminAuthAppService.login(request)));
  }

  @PostMapping(Config.REFRESH_TOKEN_PATH)
  public ResponseEntity<ResponseWrapper<AdminTokenRefreshResponseDTO>> refreshToken(
      @RequestBody AdminTokenRefreshRequestDTO body) {

    return ResponseEntity.ok(ResponseWrapper.success(adminAuthAppService.refreshAccessToken(body.getRefreshToken())));
  }

  @PostMapping(Config.LOGOUT_PATH)
  public ResponseEntity<ResponseWrapper<String>> logout(@AuthenticationPrincipal AdminUserDetails admin) {
    adminAuthAppService.logout(admin.getAdminContext().getId());
    return ResponseEntity.ok(ResponseWrapper.success(Config.LOGOUT_SUCCESS_MSG));
  }
}
