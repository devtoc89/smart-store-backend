package com.smartstore.api.v1.domain.product.entity;

import java.util.UUID;

import com.smartstore.api.v1.domain.common.entity.BaseEntity;
import com.smartstore.api.v1.domain.storedfile.entity.StoredFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_image")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ProductImage extends BaseEntity {

  // reference용으로 갱신 불가
  @Setter(AccessLevel.NONE)
  @Column(name = "product_id", insertable = false, updatable = false)
  private UUID productId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "file_id", nullable = false)
  private StoredFile file;

  @Column(name = "is_main", nullable = false)
  private boolean isMain;

  @Builder.Default
  @Column(nullable = false)
  private Integer orderBy = -1;

  public void setProduct(Product product) {
    this.product = product;
    this.productId = product.getId(); // 직접 동기화
  }
}
