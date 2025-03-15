package com.smartstore.api.v1.domain.category.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.smartstore.api.v1.domain.category.entity.CategoryL1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryL1Repository extends JpaRepository<CategoryL1, UUID>, JpaSpecificationExecutor<CategoryL1> {
  // Specification + Pageable을 사용한 페이징 처리
  @SuppressWarnings("null")
  Page<CategoryL1> findAll(Specification<CategoryL1> spec, Pageable pageable);
}