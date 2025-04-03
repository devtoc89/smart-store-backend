package com.smartstore.api.v1.domain.category.entity;

import java.util.UUID;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// lombok
@Getter
@Setter
@NoArgsConstructor
@ToString
// entity
@Entity
@Immutable
@Table(name = "mv_category_hierarchy")
public class MvCategoryHierarchy {
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "level", nullable = false)
  private int level;

  @Column(name = "parent_id")
  private UUID parentId;

  @Column(name = "path", columnDefinition = "uuid[]")
  private UUID[] path;

  @Column(name = "lv1_id")
  private UUID lv1Id;

  @Column(name = "lv2_id")
  private UUID lv2Id;

  @Column(name = "lv3_id")
  private UUID lv3Id;

}
