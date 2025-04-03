package com.smartstore.api.v1.domain.category.query.dsl.impl;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smartstore.api.v1.domain.category.entity.Category;
import com.smartstore.api.v1.domain.category.entity.QCategory;
import com.smartstore.api.v1.domain.category.query.dsl.CategoryRepositoryQuerydsl;
import com.smartstore.api.v1.domain.common.wrapper.DataWithChild;
import com.smartstore.api.v1.domain.common.wrapper.helper.DataWithChildSort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryQuerydsl {

  private final JPAQueryFactory query;

  public static final Comparator<Category> CATEGORY_COMPARATOR = Comparator
      .comparing(Category::getOrderBy, Comparator.nullsLast(Integer::compareTo))
      .thenComparing(Category::getCreatedAt, Comparator.nullsLast(ZonedDateTime::compareTo));

  public List<DataWithChild<Category, DataWithChild<Category, Category>>> fetchCategoryTree(boolean needSort) {
    return fetchCategoryTree(needSort, false);
  }

  public List<DataWithChild<Category, DataWithChild<Category, Category>>> fetchCategoryTree(boolean needSort,
      boolean needDeleted) {

    var l1 = new QCategory("l1");
    var l2 = new QCategory("l2");
    var l3 = new QCategory("l3");

    BooleanBuilder where = new BooleanBuilder();
    where.and(l1.isDeleted.eq(needDeleted)).and(l1.level.eq(1));
    where.and(l2.isDeleted.eq(needDeleted).or(l2.isDeleted.isNull())).and(l2.level.eq(2));
    where.and(l3.isDeleted.eq(needDeleted).or(l3.isDeleted.isNull())).and(l3.level.eq(3));

    List<Tuple> results = query
        .select(l1, l2, l3)
        .from(l1)
        .leftJoin(l2).on(l2.parentId.eq(l1.id))
        .leftJoin(l3).on(l3.parentId.eq(l2.id))
        .where(where)
        .fetch();

    // 결과 재조립 (중복 제거 + 트리 구성)
    Map<UUID, DataWithChild<Category, DataWithChild<Category, Category>>> l1Map = new LinkedHashMap<>();
    Map<UUID, DataWithChild<Category, Category>> l2Map = new LinkedHashMap<>();

    for (Tuple tuple : results) {
      var l1Entity = tuple.get(l1);
      var l2Entity = tuple.get(l2);
      var l3Entity = tuple.get(l3);

      // L1 처리
      var l1Saved = l1Map.computeIfAbsent(
          l1Entity.getId(),
          k -> new DataWithChild<Category, DataWithChild<Category, Category>>(l1Entity, new ArrayList<>()));

      // L2 처리
      if (l2Entity != null) {
        var l2Saved = l2Map.computeIfAbsent(
            l2Entity.getId(),
            k -> {
              var l2Child = new DataWithChild<Category, Category>(l2Entity, new ArrayList<>());
              l1Saved.getChildren().add(l2Child);
              return l2Child;
            });

        if (l3Entity != null) {
          l2Saved.getChildren().add(l3Entity);
        }
      }
    }

    List<DataWithChild<Category, DataWithChild<Category, Category>>> res = new ArrayList<>(l1Map.values());

    if (needSort) {
      DataWithChildSort.sort(res, CATEGORY_COMPARATOR, true);
    }

    return res;
  }
}