package com.smartstore.api.v1.domain.storedfile.entity;

import com.smartstore.api.v1.domain.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "file")
public class StoredFile extends BaseEntity {

  @Column(name = "key", nullable = false, length = 512)
  private String key;

  @Column(name = "original_filename", length = 255)
  private String originalFilename;

  @Column(name = "content_type", length = 100)
  private String contentType;

  @Column(name = "file_size")
  private Long fileSize;

  @Builder.Default
  @Column(name = "is_uploaded", nullable = false)
  private boolean isUploaded = false;

  public void markUploaded() {
    isUploaded = true;
  }

}
