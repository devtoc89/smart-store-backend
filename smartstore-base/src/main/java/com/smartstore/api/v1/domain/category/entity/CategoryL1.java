package com.smartstore.api.v1.domain.category.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.smartstore.api.v1.common.domain.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "category_l1")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CategoryL1 extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "categoryL1", cascade = CascadeType.ALL)
  private List<CategoryL2> subCategories = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    if (!super.equals(o))
      return false;
    CategoryL1 category = (CategoryL1) o;
    return name.equals(category.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

}