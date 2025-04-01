package com.smartstore.api.v1.domain.category.repository.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smartstore.api.v1.domain.category.entity.CategoryL1;
import com.smartstore.api.v1.domain.category.entity.CategoryL2;
import com.smartstore.api.v1.domain.category.entity.CategoryNode;
import com.smartstore.api.v1.domain.category.entity.QCategoryL1;
import com.smartstore.api.v1.domain.category.entity.QCategoryL2;
import com.smartstore.api.v1.domain.category.entity.QCategoryNode;
import com.smartstore.api.v1.domain.category.repository.querydsl.CategoryRepositoryQuerydsl;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryQuerydsl {

  private final JPAQueryFactory query;

  public static final Comparator<CategoryL1> BY_ORDER_THEN_CREATED_AT_L1 = Comparator
      .comparing(CategoryL1::getOrderBy, Comparator.nullsLast(Integer::compareTo))
      .thenComparing(CategoryL1::getCreatedAt, Comparator.nullsLast(ZonedDateTime::compareTo));

  public static final Comparator<CategoryL2> BY_ORDER_THEN_CREATED_AT_L2 = Comparator
      .comparing(CategoryL2::getOrderBy, Comparator.nullsLast(Integer::compareTo))
      .thenComparing(CategoryL2::getCreatedAt, Comparator.nullsLast(ZonedDateTime::compareTo));

  public static final Comparator<CategoryNode> BY_ORDER_THEN_CREATED_AT_NODE = Comparator
      .comparing(CategoryNode::getOrderBy, Comparator.nullsLast(Integer::compareTo))
      .thenComparing(CategoryNode::getCreatedAt, Comparator.nullsLast(ZonedDateTime::compareTo));

  public List<CategoryL1> fetchCategoryTree(boolean needSort) {
    QCategoryL1 l1 = QCategoryL1.categoryL1;
    QCategoryL2 l2 = QCategoryL2.categoryL2;
    QCategoryNode node = QCategoryNode.categoryNode;

    List<Tuple> results = query
        .select(l1, l2, node)
        .from(l1)
        .leftJoin(l1.subCategories, l2)
        .leftJoin(l2.subCategories, node)
        .where(
            l1.isDeleted.eq(false).or(l1.isDeleted.isNull()),
            l2.isDeleted.eq(false).or(l2.isDeleted.isNull()),
            node.isDeleted.eq(false).or(node.isDeleted.isNull()))
        .fetch();

    // 결과 재조립 (중복 제거 + 트리 구성)
    Map<UUID, CategoryL1> l1Map = new LinkedHashMap<>();

    for (Tuple tuple : results) {
      CategoryL1 l1Entity = tuple.get(l1);
      CategoryL2 l2Entity = tuple.get(l2);
      CategoryNode nodeEntity = tuple.get(node);

      // L1 처리
      CategoryL1 l1Saved = l1Map.computeIfAbsent(
          l1Entity.getId(),
          k -> {
            l1Entity.setSubCategories(new ArrayList<>()); // 자식 리스트 초기화
            return l1Entity;
          });

      // L2 처리
      if (l2Entity != null) {
        List<CategoryL2> l2List = l1Saved.getSubCategories();
        CategoryL2 l2Saved = l2List.stream()
            .filter(e -> e.getId().equals(l2Entity.getId()))
            .findFirst()
            .orElseGet(() -> {
              l2Entity.setSubCategories(new ArrayList<>()); // 자식 리스트 초기화
              l2List.add(l2Entity);
              return l2Entity;
            });

        // Node 처리
        if (nodeEntity != null) {
          List<CategoryNode> nodeList = l2Saved.getSubCategories();
          if (nodeList.stream().noneMatch(n -> n.getId().equals(nodeEntity.getId()))) {
            nodeList.add(nodeEntity);
          }
        }
      }
    }

    List<CategoryL1> res = new ArrayList<>(l1Map.values());

    if (needSort) {
      res.sort(BY_ORDER_THEN_CREATED_AT_L1);
      res.forEach(categoryl1 -> {
        categoryl1.getSubCategories().sort(BY_ORDER_THEN_CREATED_AT_L2);
        categoryl1.getSubCategories()
            .forEach(categoryl2 -> categoryl2.getSubCategories().sort(BY_ORDER_THEN_CREATED_AT_NODE));
      });

    }

    return res;
  }
}