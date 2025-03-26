package com.smartstore.api.v1.common.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.smartstore.api.v1.common.dto.CustomErrorResponseDTO;
import com.smartstore.api.v1.common.exception.BaseException;
import com.smartstore.api.v1.common.utils.exception.ExceptionUtil;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<CustomErrorResponseDTO> handleNoResourceFoundException(NoResourceFoundException ex) {
    log.warn("error occured", ex);
    CustomErrorResponseDTO errorResponse = new CustomErrorResponseDTO(
        HttpStatus.NOT_FOUND.value(),
        "유효하지 않은 URL 요청입니다.",
        List.of(ex.getMessage()),
        LocalDateTime.now());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<CustomErrorResponseDTO> handleNoSuchElementException(NoSuchElementException ex) {
    CustomErrorResponseDTO errorResponse = new CustomErrorResponseDTO(
        HttpStatus.NO_CONTENT.value(),
        "요청한 데이터가 없습니다.",
        List.of(ex.getMessage()),
        LocalDateTime.now());

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
  }

  @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class })
  public ResponseEntity<CustomErrorResponseDTO> handleValidationException(Exception ex) {
    List<String> errors = new ArrayList<>();

    if (ex instanceof MethodArgumentNotValidException methodEx) {
      // `@Valid`로 검증 시 발생 (JSON 요청 Body)
      errors = methodEx.getBindingResult()
          .getFieldErrors()
          .stream()
          .map(ExceptionUtil::formatFieldError)
          .toList();

    } else if (ex instanceof BindException bindEx) {
      // `@ModelAttribute` 바인딩 검증 실패 시 발생 (폼 요청)
      errors = bindEx.getBindingResult()
          .getFieldErrors()
          .stream()
          .map(ExceptionUtil::formatFieldError)
          .toList();

    } else if (ex instanceof ConstraintViolationException constraintEx) {
      // `@Validated`가 붙은 PathVariable, RequestParam 검증 실패 시 발생
      errors = constraintEx.getConstraintViolations()
          .stream()
          .map(ExceptionUtil::formatConstraintViolation)
          .toList();
    }

    CustomErrorResponseDTO errorResponse = new CustomErrorResponseDTO(
        HttpStatus.BAD_REQUEST.value(),
        "잘못된 요청입니다.",
        errors,
        LocalDateTime.now());

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler({ BaseException.class })
  public ResponseEntity<CustomErrorResponseDTO> handleBaseException(BaseException ex) {
    CustomErrorResponseDTO errorResponse = new CustomErrorResponseDTO(
        ex.getStatus().value(),
        ex.getMessage(),
        List.of(), // 세부 에러 없음
        LocalDateTime.now());
    return ResponseEntity.status(ex.getStatus()).body(errorResponse);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<CustomErrorResponseDTO> handleDataIntegrityViolationException(BaseException ex) {
    // 절대로 db 문제가 밖으로 표시되면 안됨. sentry등 모니터링 툴로만 표시.
    CustomErrorResponseDTO errorResponse = new CustomErrorResponseDTO(
        ex.getStatus().value(),
        "시스템에 에러가 발생하였습니다.",
        List.of(), // 세부 에러 없음
        LocalDateTime.now());
    return ResponseEntity.status(ex.getStatus()).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomErrorResponseDTO> handleException(Exception ex) {
    // TODO: 1. 500에러의 경우, 보안에 취약하므로, PROD의 경우, monitoring 서버에 전송으로 변경 필요 (ex.
    // Sentry), 2. 고객에게는 500외의(503 등) 상태 코드 제공 및 메세지 변경 필요
    log.error("error occured", ex);

    CustomErrorResponseDTO errorResponse = new CustomErrorResponseDTO(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "서버 내부 오류가 발생했습니다.",
        List.of(ex.getMessage()),
        LocalDateTime.now());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}