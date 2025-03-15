package com.smartstore.api.v1.domain.product.entity;

import java.util.Objects;
import java.util.UUID;

import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.common.domain.entity.BaseEntity;
import com.smartstore.api.v1.domain.product.vo.ProductVO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private Integer price;

  @Column(nullable = false)
  private UUID categoryId;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    if (!super.equals(o))
      return false;
    Product product = (Product) o;
    return price.equals(product.price) &&
        name.equals(product.name) &&
        categoryId.equals(product.categoryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  public void applyUpdate(ProductVO vo) {
    setName(vo.getName());
    setPrice(vo.getPrice());
    setCategoryId(vo.getCategoryId());
  }

  public void applyPartialUpdate(ProductVO vo) {
    if (!ObjectUtils.isEmpty(vo.getName())) {
      setName(vo.getName());
    }
    if (!ObjectUtils.isEmpty(vo.getPrice())) {
      setPrice(vo.getPrice());
    }
    if (!ObjectUtils.isEmpty(vo.getCategoryId())) {
      setCategoryId(vo.getCategoryId());
    }
  }
}