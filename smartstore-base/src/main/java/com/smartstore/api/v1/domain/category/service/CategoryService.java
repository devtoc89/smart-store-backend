package com.smartstore.api.v1.domain.category.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smartstore.api.v1.domain.category.entity.CategoryL1;
import com.smartstore.api.v1.domain.category.entity.CategoryL2;
import com.smartstore.api.v1.domain.category.entity.CategoryNode;
import com.smartstore.api.v1.domain.category.repository.CategoryRepository;
import com.smartstore.api.v1.domain.category.vo.CategoryL1VO;
import com.smartstore.api.v1.domain.category.vo.CategoryL2VO;
import com.smartstore.api.v1.domain.category.vo.CategoryNodeVO;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;
import com.smartstore.api.v1.domain.category.vo.CategoryVO.CategoryL1Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryVO.CategoryL1Nest.CategoryL2Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryVO.CategoryL1Nest.CategoryL2Nest.CategoryNodeNest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

  @PersistenceContext
  private EntityManager entityManager;

  private final CategoryRepository categoryRepository;

  private final CategoryL1Service categoryL1Service;
  private final CategoryL2Service categoryL2Service;
  private final CategoryNodeService categoryNodeService;

  public List<CategoryL1> findAllWithChildren(boolean needSort) {
    return categoryRepository.fetchCategoryTree(needSort);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryVO findAll() {
    return CategoryVO.fromEntityWithList(findAllWithChildren(true));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void putAll(CategoryVO newVO) {
    // 1. 기존 전체 트리 조회
    List<CategoryL1> existingEntities = categoryRepository.fetchCategoryTree(false);
    entityManager.clear();

    var l2Tol1IdReverseMap = new HashMap<UUID, UUID>();
    var nodeTol2IdReverseMap = new HashMap<UUID, UUID>();

    var voMapL1 = new HashMap<UUID, CategoryL1VO>();
    var voMapL2 = new HashMap<UUID, CategoryL2VO>();
    var voMapNode = new HashMap<UUID, CategoryNodeVO>();

    for (CategoryL1Nest l1 : newVO.getCategoryL1Nest()) {
      voMapL1.put(l1.getCategoryL1VO().getBase().getId(), l1.getCategoryL1VO());
      for (CategoryL2Nest l2 : l1.getChildren()) {
        voMapL2.put(l2.getCategoryL2VO().getBase().getId(), l2.getCategoryL2VO());
        for (CategoryNodeNest node : l2.getChildren()) {
          voMapNode.put(node.getCategoryNodeVO().getBase().getId(), node.getCategoryNodeVO());
        }
      }
    }

    var entityMapL1 = new HashMap<UUID, CategoryL1>();
    var entityMapL2 = new HashMap<UUID, CategoryL2>();
    var entityMapNode = new HashMap<UUID, CategoryNode>();

    for (CategoryL1 l1 : existingEntities) {
      entityMapL1.put(l1.getId(), l1);
      for (CategoryL2 l2 : l1.getSubCategories()) {
        entityMapL2.put(l2.getId(), l2);
        l2Tol1IdReverseMap.put(l2.getId(), l1.getId());
        for (CategoryNode node : l2.getSubCategories()) {
          entityMapNode.put(node.getId(), node);
          nodeTol2IdReverseMap.put(node.getId(), l2.getId());
        }
      }
    }
    syncEntities(
        voMapL1,
        entityMapL1,
        (id, vo) -> {
          var entity = categoryL1Service.applyCreate(vo.getBase().getId(), vo);
          entityManager.persist(entity);
          entityMapL1.put(entity.getId(), entity);
        },
        (entity, vo) -> {
          if (!categoryL1Service.isNotModified(entity, vo)) {
            categoryL1Service.replace(vo.getBase().getId(), vo);
          }
        },
        entity -> {
          categoryL1Service.delete(entity.getId());
        });

    syncEntities(
        voMapL2,
        entityMapL2,
        (id, vo) -> {
          var entity = categoryL2Service.applyCreate(vo.getBase().getId(), vo);
          entityManager.persist(entity);
          entityMapL2.put(entity.getId(), entity);
        },
        (entity, vo) -> {
          if (!vo.getCategoryL1Id().equals(l2Tol1IdReverseMap.get(entity.getId()))
              || !categoryL2Service.isNotModified(entity, vo)) {
            categoryL2Service.replace(vo.getBase().getId(), vo);
          }
        },
        entity -> {
          categoryL2Service.delete(entity.getId());
        });

    syncEntities(
        voMapNode,
        entityMapNode,
        (id, vo) -> {
          var entity = categoryNodeService.applyCreate(vo.getBase().getId(), vo);
          entityManager.persist(entity);
        },
        (entity, vo) -> {
          if (!vo.getCategoryL2Id().equals(nodeTol2IdReverseMap.get(entity.getId()))
              || !categoryNodeService.isNotModified(entity, vo)) {
            categoryNodeService.replace(vo.getBase().getId(), vo);
          }
        },
        entity -> {
          categoryNodeService.delete(entity.getId());
        });

    entityManager.flush();
  }

  private <ID, VO, ENTITY> void syncEntities(
      Map<ID, VO> newMap,
      Map<ID, ENTITY> oldMap,
      BiConsumer<ID, VO> createFn,
      BiConsumer<ENTITY, VO> updateFn,
      Consumer<ENTITY> deleteFn) {
    Set<ID> voIds = newMap.keySet();
    Set<ID> entityIds = oldMap.keySet();

    Set<ID> toCreate = new HashSet<>(voIds);
    toCreate.removeAll(entityIds);

    Set<ID> toUpdate = new HashSet<>(voIds);
    toUpdate.retainAll(entityIds);

    Set<ID> toDelete = new HashSet<>(entityIds);
    toDelete.removeAll(voIds);

    for (ID id : toDelete) {
      ENTITY entity = oldMap.get(id);
      deleteFn.accept(entity);
    }

    for (ID id : toUpdate) {
      ENTITY entity = oldMap.get(id);
      VO vo = newMap.get(id);
      updateFn.accept(entity, vo);
    }

    for (ID id : toCreate) {
      VO vo = newMap.get(id);
      createFn.accept(id, vo);
    }
  }

}
