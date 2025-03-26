package com.smartstore.api.v1.domain.product.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartstore.api.v1.domain.product.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
  @SuppressWarnings("null")
  Page<ProductImage> findAll(Specification<ProductImage> spec, Pageable pageable);

  @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.isMain = true AND pi.deletedAt IS NULL")
  Optional<ProductImage> findMain(@Param("productId") UUID productId);

  List<ProductImage> deleteByProductId(UUID id);

  List<ProductImage> findByProductId(UUID id);
}