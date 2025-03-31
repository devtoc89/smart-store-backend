package com.smartstore.api.v1.domain.category.entity;

import java.util.ArrayList;
import java.util.List;

import com.smartstore.api.v1.common.domain.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = "subCategories")
@Entity
@Table(name = "category_l2")
public class CategoryL2 extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "category_l1_id", nullable = false)
  private CategoryL1 categoryL1;

  @Builder.Default
  @OneToMany(mappedBy = "categoryL2", cascade = CascadeType.ALL)
  private List<CategoryNode> subCategories = new ArrayList<>();

  @Builder.Default
  @Column(nullable = false)
  private Integer orderBy = -1;

}