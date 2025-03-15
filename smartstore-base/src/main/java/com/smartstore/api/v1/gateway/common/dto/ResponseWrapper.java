package com.smartstore.api.v1.gateway.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWrapper<T> {
  private boolean ok;
  private T data;
  private String message;

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
