package com.smartstore.api.v1.common.dto;

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
  private boolean ok;
  @EqualsAndHashCode.Include
  private String message;
  // DATA의 동일 여부는 Data 자체에 대한 equal을 수행할 것!
  @EqualsAndHashCode.Exclude
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

  public static <T> ResponseWrapper<T> success(T data) {
    return new ResponseWrapper<>(data);
  }

  public static <T> ResponseWrapper<T> success(T data, String message) {
    return new ResponseWrapper<>(data, message);
  }
}
