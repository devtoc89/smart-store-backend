package com.smartstore.api.v1.domain.category.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstore.api.v1.domain.category.entity.MvCategoryHierarchy;

public interface MvCategoryHierarchyRepository extends JpaRepository<MvCategoryHierarchy, UUID> {
  List<MvCategoryHierarchy> findAll(Specification<MvCategoryHierarchy> spec);
}