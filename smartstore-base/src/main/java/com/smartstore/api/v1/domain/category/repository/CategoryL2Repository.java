package com.smartstore.api.v1.domain.category.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.smartstore.api.v1.domain.category.entity.CategoryL2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryL2Repository extends JpaRepository<CategoryL2, UUID>, JpaSpecificationExecutor<CategoryL2> {
  // Specification + Pageable을 사용한 페이징 처리
  @SuppressWarnings("null")
  Page<CategoryL2> findAll(Specification<CategoryL2> spec, Pageable pageable);
}