package com.smartstore.api.v1.domain.category.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstore.api.v1.domain.category.entity.Category;
import com.smartstore.api.v1.domain.category.query.dsl.CategoryRepositoryQuerydsl;

public interface CategoryRepository extends JpaRepository<Category, UUID>, CategoryRepositoryQuerydsl {
  // Specification + Pageable을 사용한 페이징 처리
  @SuppressWarnings("null")
  Page<Category> findAll(Specification<Category> spec, Pageable pageable);
}