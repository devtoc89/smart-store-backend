package com.smartstore.api.v1.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ResponseWrapper<T> {
  @EqualsAndHashCode.Include
  @Schema(description = "정상여부", example = "true")
  private boolean ok;

  @EqualsAndHashCode.Include
  @Schema(description = "처리 메세지", example = "정상 처리되었습니다.")
  private String message;
  // DATA의 동일 여부는 Data 자체에 대한 equal을 수행할 것!
  @EqualsAndHashCode.Exclude
  @Schema(description = "취득 결과 데이터", example = "{\"name\":\"이름\"}")
  private T data;

  public ResponseWrapper(T data) {
    this.ok = true;
    this.data = data;
    this.message = "요청이 성공적으로 처리되었습니다.";
  }

  public ResponseWrapper(T data, String message) {
    this.ok = true;
    this.data = data;
    this.message = message;
  }

  public ResponseWrapper(String message, boolean ok) {
    this.ok = ok;
    this.data = data;
    this.message = message;
  }

  public static <T> ResponseWrapper<T> success(T data) {
    return new ResponseWrapper<>(data);
  }

  public static <T> ResponseWrapper<T> success(T data, String message) {
    return new ResponseWrapper<>(data, message);
  }

  public static <T> ResponseWrapper<T> fail(String message) {
    return new ResponseWrapper<>(message, false);
  }
}
