package com.smartstore.api.v1.domain.category.entity;

import java.util.Objects;

import com.smartstore.api.v1.common.domain.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "category_node")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CategoryNode extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "category_l2_id", nullable = false)
  private CategoryL2 categoryL2;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    if (!super.equals(o))
      return false;
    CategoryNode category = (CategoryNode) o;
    return name.equals(category.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

}