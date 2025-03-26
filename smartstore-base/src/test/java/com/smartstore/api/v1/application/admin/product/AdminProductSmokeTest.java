package com.smartstore.api.v1.application.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smartstore.api.v1.application.admin.product.service.AdminProductAppService;

@SpringBootTest
class AdminProductSmokeTest {
  @Autowired
  private AdminProductController adminProductController;
  @Autowired
  private AdminProductAppService adminProductAppService;

  @Test
  void contextLoads() {
    assertThat(adminProductController).isNotNull();
    assertThat(adminProductAppService).isNotNull();
  }
}
