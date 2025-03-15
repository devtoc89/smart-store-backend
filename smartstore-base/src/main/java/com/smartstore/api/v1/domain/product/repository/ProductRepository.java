package com.smartstore.api.v1.domain.product.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.smartstore.api.v1.domain.product.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
  // Specification + Pageable을 사용한 페이징 처리
  @SuppressWarnings("null")
  Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}