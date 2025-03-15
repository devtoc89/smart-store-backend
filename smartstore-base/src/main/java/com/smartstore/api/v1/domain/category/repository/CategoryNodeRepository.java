package com.smartstore.api.v1.domain.category.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.smartstore.api.v1.domain.category.entity.CategoryNode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryNodeRepository
    extends JpaRepository<CategoryNode, UUID>, JpaSpecificationExecutor<CategoryNode> {
  // Specification + Pageable을 사용한 페이징 처리
  @SuppressWarnings("null")
  Page<CategoryNode> findAll(Specification<CategoryNode> spec, Pageable pageable);
}