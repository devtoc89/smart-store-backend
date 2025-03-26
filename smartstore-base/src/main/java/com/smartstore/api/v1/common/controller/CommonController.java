package com.smartstore.api.v1.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartstore.api.v1.common.constants.message.CommonMessage;
import com.smartstore.api.v1.common.dto.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

class Config {
  protected static final String BASE_URL = "/";

  private Config() {
    throw new UnsupportedOperationException(CommonMessage.CANNOT_INITIALIZE_CONSTANTS_CLASS_MSG);
  }
}

@RestController
@RequestMapping(Config.BASE_URL)
@Tag(name = "Common API", description = "공통 API")
public class CommonController {

  @GetMapping("health")
  @Operation(summary = "health 체크", description = "health 체크")
  @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseWrapper.class)))
  public ResponseEntity<ResponseWrapper<String>> searchProducts() {
    return ResponseEntity.ok(ResponseWrapper.success("ok"));
  }

  @GetMapping("favicon.ico")
  public ResponseEntity<Void> favicon() {
    return ResponseEntity.noContent().build();
  }
}
