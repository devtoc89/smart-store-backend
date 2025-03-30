package com.smartstore.api.v1.domain.category.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstore.api.v1.domain.category.entity.CategoryL1;
import com.smartstore.api.v1.domain.category.repository.querydsl.CategoryRepositoryQuerydsl;

public interface CategoryRepository extends JpaRepository<CategoryL1, UUID>, CategoryRepositoryQuerydsl {
}