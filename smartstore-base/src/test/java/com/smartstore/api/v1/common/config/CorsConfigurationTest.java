package com.smartstore.api.v1.common.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
// 테스트 환경에서 동적으로 Origin을 설정 (쉼표로 구분된 목록 사용)
@TestPropertySource(properties = "app.cors.allowed-origins=http://localhost:8080,http://example.com")
class CorsConfigurationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testCorsHeadersWithAllowedOrigin() throws Exception {
    mockMvc.perform(options("/api/v1/admin/products")
        .header("Origin", "http://localhost:8080")
        .header("Access-Control-Request-Method", "GET"))
        .andExpect(status().isOk())
        .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:8080"));
  }

  @Test
  void testCorsHeadersWithNotAllowedOrigin() throws Exception {
    // 만약 요청 Origin이 설정된 목록에 없다면 CORS 설정에 따라 헤더가 반환되지 않을 수 있음
    mockMvc.perform(options("/api/v1/admin/products")
        .header("Origin", "http://notallowed.com")
        .header("Access-Control-Request-Method", "GET"))
        .andExpect(status().is4xxClientError())
        .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
  }
}
