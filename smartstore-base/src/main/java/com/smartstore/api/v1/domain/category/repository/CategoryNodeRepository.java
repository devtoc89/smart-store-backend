package com.smartstore.api.v1.domain.category.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.smartstore.api.v1.domain.category.entity.CategoryNode;

public interface CategoryNodeRepository
    extends JpaRepository<CategoryNode, UUID>, JpaSpecificationExecutor<CategoryNode> {

}