package com.smartstore.api.v1.domain.storedfile.vo;

import org.springframework.data.domain.Page;

import com.smartstore.api.v1.domain.common.vo.BaseEntityVO;
import com.smartstore.api.v1.domain.storedfile.entity.StoredFile;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoredFileVO {
  private BaseEntityVO base;
  private String key;
  private String filename;
  private String contentType;
  private Long fileSize;
  private Boolean isUploaded;

  public static StoredFileVO fromEntity(StoredFile entity) {
    return StoredFileVO.builder()
        .base(BaseEntityVO.fromEntity(
            entity))
        .key(entity.getKey())
        .filename(entity.getOriginalFilename())
        .fileSize(entity.getFileSize())
        .contentType(entity.getContentType())
        .isUploaded(entity.isUploaded())
        .build();
  }

  public static Page<StoredFileVO> fromEntityWithPage(Page<StoredFile> pageEntity) {
    return pageEntity.map(v -> fromEntity(v));
  }

  public String getFullUrl(String cloudFrontUrl) {
    if (cloudFrontUrl == null || key == null)
      return "";
    return cloudFrontUrl.endsWith("/") ? cloudFrontUrl + key : cloudFrontUrl + "/" + key;
  }
}
