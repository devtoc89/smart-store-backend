package com.smartstore.api.v1.domain.category.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.domain.category.entity.Category;
import com.smartstore.api.v1.domain.category.repository.CategoryRepository;
import com.smartstore.api.v1.domain.category.vo.CategoryFilterConditionVO;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO.CategoryL1Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO.CategoryL1Nest.CategoryL2Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryNestVO.CategoryL1Nest.CategoryL2Nest.CategoryL3Nest;
import com.smartstore.api.v1.domain.category.vo.CategoryVO;
import com.smartstore.api.v1.domain.common.wrapper.DataWithChild;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

  @PersistenceContext
  private EntityManager entityManager;

  private static final int CATEGORY_TREE_DEPTH = 3;
  private final CategoryRepository categoryRepository;

  public Category applyCreate(UUID id, CategoryVO vo) {
    return Category.builder()
        .id(id)
        .name(vo.getName())
        .orderBy(vo.getOrderBy())
        .level(vo.getLevel())
        .parentId(vo.getParentId())
        .build();
  }

  public Category applyUpdate(Category entity, CategoryVO vo) {
    entity.setName(vo.getName());
    entity.setOrderBy(vo.getOrderBy());
    // entity.setLevel(vo.getLevel());
    entity.setParentId(vo.getParentId());
    return entity;
  }

  public Category applyPartialUpdate(Category entity, CategoryVO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      entity.setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getOrderBy())) {
      entity.setOrderBy(vo.getOrderBy());
    }
    // if (!ObjectUtils.isEmpty(vo.getLevel())) {
    // entity.setLevel(vo.getLevel());
    // }
    if (!ObjectUtils.isEmpty(vo.getParentId())) {
      entity.setParentId(vo.getParentId());
    }
    return entity;
  }

  public boolean isNotModified(Category entity, CategoryVO vo) {
    if (!ObjectUtils.nullSafeEquals(entity.getName(), vo.getName())) {
      return false;
    }
    if (!ObjectUtils.nullSafeEquals(entity.getOrderBy(), vo.getOrderBy())) {
      return false;
    }
    // if (!ObjectUtils.nullSafeEquals(entity.getLevel(), vo.getLevel())) {
    // return false;
    // }
    if (!ObjectUtils.nullSafeEquals(entity.getParentId(), vo.getParentId())) {
      return false;
    }
    return true;
  }

  private static <K, V> List<Map<K, V>> newHashMapList(int count) {
    List<Map<K, V>> list = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      list.add(new HashMap<>());
    }
    return list;
  }

  private Category findByIdOrExcept(UUID id) {
    return categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 카테고리가 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public Page<CategoryVO> findManyByCondition(CategoryFilterConditionVO condition, Pageable pageable) {
    return CategoryVO.fromEntityWithPage(categoryRepository.findAll(condition.toSpecification(), pageable));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryVO findById(UUID id) {
    return CategoryVO.fromEntity(findByIdOrExcept(id));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public CategoryNestVO findAll() {
    return CategoryNestVO.fromEntityWithList(categoryRepository.fetchCategoryTree(true));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExists(UUID id) {
    return categoryRepository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryVO create(UUID id, CategoryVO vo) {
    var entity = applyCreate(id, vo);
    return CategoryVO.fromEntity(categoryRepository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryVO replace(UUID id, CategoryVO vo) {
    var entity = applyUpdate(findByIdOrExcept(id), vo);
    return CategoryVO.fromEntity(
        categoryRepository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryVO modify(UUID id, CategoryVO vo) {
    var entity = applyPartialUpdate(findByIdOrExcept(id), vo);
    return CategoryVO.fromEntity(categoryRepository.save(entity));
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public CategoryVO delete(UUID id) {
    Category entity = findByIdOrExcept(id);
    entity.markDelete();

    return CategoryVO.fromEntity(categoryRepository.save(entity));
  }

  private List<Map<UUID, CategoryVO>> makeVOMapList(CategoryNestVO vo) {

    List<Map<UUID, CategoryVO>> voMapList = newHashMapList(CATEGORY_TREE_DEPTH);

    for (CategoryL1Nest l1 : vo.getCategoryL1Nest()) {
      voMapList.get(0).put(l1.getCategory().getBase().getId(), l1.getCategory());
      for (CategoryL2Nest l2 : l1.getChildren()) {
        voMapList.get(1).put(l2.getCategory().getBase().getId(), l2.getCategory());
        for (CategoryL3Nest node : l2.getChildren()) {
          voMapList.get(2).put(node.getCategory().getBase().getId(), node.getCategory());
        }
      }
    }

    return voMapList;
  }

  private List<Map<UUID, Category>> makeEntityMapList(
      List<DataWithChild<Category, DataWithChild<Category, Category>>> existingEntities) {
    List<Map<UUID, Category>> entityMapList = newHashMapList(CATEGORY_TREE_DEPTH);

    for (DataWithChild<Category, DataWithChild<Category, Category>> l1 : existingEntities) {
      entityMapList.get(0).put(l1.getData().getId(), l1.getData());
      for (DataWithChild<Category, Category> l2 : l1.getChildren()) {
        entityMapList.get(1).put(l2.getData().getId(), l2.getData());
        for (Category node : l2.getChildren()) {
          entityMapList.get(2).put(node.getId(), node);
        }
      }
    }
    return entityMapList;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void putAll(CategoryNestVO newVO) {
    // 1. 기존 전체 트리 조회
    List<DataWithChild<Category, DataWithChild<Category, Category>>> existingEntities = categoryRepository
        .fetchCategoryTree(false, true);

    List<Map<UUID, CategoryVO>> voMapList = makeVOMapList(newVO);
    List<Map<UUID, Category>> entityMapList = makeEntityMapList(existingEntities);

    for (int i = 0; i < CATEGORY_TREE_DEPTH; i++) {
      final int index = i;

      syncEntities(
          voMapList.get(index),
          entityMapList.get(index),
          (id, vo) -> {
            var entity = applyCreate(vo.getBase().getId(), vo);
            entityManager.persist(entity);
            entityMapList.get(index).put(entity.getId(), entity);
          },
          (entity, vo) -> {
            if (!isNotModified(entity, vo)) {
              applyUpdate(entity, vo);
            }
          },
          entity -> {
            entity.markDelete();
          });
    }

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

    for (ID id : toCreate) {
      VO vo = newMap.get(id);
      createFn.accept(id, vo);
    }
    for (ID id : toUpdate) {
      ENTITY entity = oldMap.get(id);
      VO vo = newMap.get(id);
      updateFn.accept(entity, vo);
    }
    for (ID id : toDelete) {
      ENTITY entity = oldMap.get(id);
      deleteFn.accept(entity);
    }
  }

}
