package com.smartstore;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class SmartStoreApplicationTest {

  @Test
  @DisplayName("기동 테스트")
  void contextLoads() {
  }
}