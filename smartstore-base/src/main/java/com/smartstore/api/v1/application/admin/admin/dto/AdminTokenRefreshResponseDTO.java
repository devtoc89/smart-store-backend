package com.smartstore.api.v1.application.admin.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@Schema(description = "어드민 토큰 DTO")
public class AdminTokenRefreshResponseDTO {
  private String accessToken;
  private String refreshToken;
}
