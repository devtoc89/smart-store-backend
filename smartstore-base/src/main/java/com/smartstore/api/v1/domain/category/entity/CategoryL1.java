package com.smartstore.api.v1.domain.category.entity;

import java.util.ArrayList;
import java.util.List;

import com.smartstore.api.v1.common.domain.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "category_l1")
public class CategoryL1 extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @Builder.Default
  @OneToMany(mappedBy = "categoryL1", cascade = CascadeType.ALL)
  private List<CategoryL2> subCategories = new ArrayList<>();

  @Builder.Default
  @Column(nullable = false)
  private Integer orderBy = -1;

}