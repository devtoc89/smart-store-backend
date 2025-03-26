package com.smartstore.api.v1.domain.category.entity;

import com.smartstore.api.v1.common.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category_node")
public class CategoryNode extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "category_l2_id", nullable = false)
  private CategoryL2 categoryL2;

  @Builder.Default
  @Column(nullable = false)
  private Integer orderBy = -1;
}