package com.smartstore.api.v1.domain.product.entity;

import java.util.List;
import java.util.UUID;

import com.smartstore.api.v1.domain.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class Product extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @Builder.Default
  @Column(nullable = false)
  private String description = "unused";

  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false)
  private UUID categoryId;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductImage> images;

}