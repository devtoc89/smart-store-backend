package com.smartstore.api.v1.common.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답 DTO")
public class CustomErrorResponseDTO {
  @Schema(description = "정상여부", example = "false")
  private Boolean ok = false;

  @Schema(description = "HTTP 상태 코드", example = "400")
  private int status;

  @Schema(description = "에러 메시지", example = "잘못된 요청입니다.")
  private String message;

  @Schema(description = "발생한 필드별 에러 목록", example = "[\"{필드}: {에러내용}\"]")
  private List<String> errors;

  @Schema(description = "에러 발생 시간", example = "2025-03-15T10:15:30")
  private LocalDateTime timestamp;

  public CustomErrorResponseDTO(int status, String message, List<String> errors, LocalDateTime timestamp) {
    this.status = status;
    this.message = message;
    this.errors = errors;
    this.timestamp = timestamp;
  }

  public int getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }

  public List<String> getErrors() {
    return errors;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }
}
