package com.smartstore.api.v1.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.domain.category.entity.MvCategoryHierarchy;
import com.smartstore.api.v1.domain.category.repository.MvCategoryHierarchyRepository;
import com.smartstore.api.v1.domain.category.vo.MvCategoryHierarchyConditionVO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MvCategoryHierarchyService {

  @PersistenceContext
  private EntityManager entityManager;

  private final MvCategoryHierarchyRepository mvCategoryHierarchyRepository;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public List<MvCategoryHierarchy> findManyByCondition(MvCategoryHierarchyConditionVO condition) {
    return mvCategoryHierarchyRepository.findAll(condition.toSpecification());
  }

}
