package com.smartstore.api.v1.domain.category.entity;

import java.util.UUID;

import com.smartstore.api.v1.domain.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "category")
public class Category extends BaseEntity {

  @ToString.Include
  @Column(nullable = false)
  private String name;

  @ToString.Include
  @Column(nullable = false)
  private Integer level;

  @ToString.Include
  private UUID parentId;

  @ToString.Include
  @Builder.Default
  @Column(nullable = false)
  private Integer orderBy = -1;
}