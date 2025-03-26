package com.smartstore.api.v1.common.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

// TODO: Monitoring 후, cache 대상 선정 필요
@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager(); // 캐시 이름 지정
    cacheManager.setCaffeine(caffeineConfig()); // 캐시 설정 적용
    return cacheManager;
  }

  @Bean
  public Caffeine<Object, Object> caffeineConfig() {
    return Caffeine.newBuilder()
        .initialCapacity(100) // 처음 캐시에 저장할 수 있는 객체 수
        .maximumSize(500) // 최대 캐시 저장 개수
        .expireAfterWrite(10, TimeUnit.MINUTES) // 10분 후 캐시 만료
        .expireAfterAccess(5, TimeUnit.MINUTES) // 5분 동안 사용되지 않으면 삭제
        .softValues()
        .recordStats(); // 캐시 통계 기록
  }
}