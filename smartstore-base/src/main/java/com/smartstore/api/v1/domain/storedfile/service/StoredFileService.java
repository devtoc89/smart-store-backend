package com.smartstore.api.v1.domain.storedfile.service;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.smartstore.api.v1.common.infrastructure.S3PresignedUrlService;
import com.smartstore.api.v1.common.utils.file.FileNameUtil;
import com.smartstore.api.v1.common.utils.file.MimeTypeUtil;
import com.smartstore.api.v1.domain.storedfile.entity.StoredFile;
import com.smartstore.api.v1.domain.storedfile.repository.StoredFileRepository;
import com.smartstore.api.v1.domain.storedfile.vo.StoredFileVO;
import com.smartstore.api.v1.domain.storedfile.vo.StoredPresignedFileDTO;

import lombok.RequiredArgsConstructor;

//TODO: 관심사 분리
@Service
@RequiredArgsConstructor
public class StoredFileService {

  private final S3PresignedUrlService s3PresignedUrlService;
  private final StoredFileRepository storedFileRepository;

  public StoredPresignedFileDTO generatePreSignedUrl(String fileName, String tag, Long size) {
    String key = FileNameUtil.generateFileName(fileName, tag);
    String contentType = MimeTypeUtil.detectContentType(fileName);
    var fileId = registerFileMetadata(fileName, key, contentType, size).getId();

    return StoredPresignedFileDTO.builder().fileId(fileId.toString()).key(key)
        .url(s3PresignedUrlService.generatePresignedPutUrl(key)).build();
  }

  public StoredFile registerFileMetadata(String originalName, String key, String contentType, Long size) {
    StoredFile file = StoredFile.builder()
        .key(key)
        .originalFilename(originalName)
        .contentType(contentType)
        .fileSize(size)
        .build();

    return storedFileRepository.save(file);
  }

  public StoredFile applyUpdate(StoredFile entity, StoredFileVO vo) {
    entity.setKey(vo.getKey());
    entity.setContentType(vo.getContentType());
    entity.setFileSize(vo.getFileSize());
    entity.setOriginalFilename(vo.getFilename());
    entity.setUploaded(vo.getIsUploaded());

    return entity;
  }

  public StoredFile applyPartialUpdate(StoredFile entity, StoredFileVO vo) {
    if (!ObjectUtils.isEmpty(vo.getKey())) {
      entity.setKey(vo.getKey());
    }
    if (!ObjectUtils.isEmpty(vo.getContentType())) {
      entity.setContentType(vo.getContentType());
    }
    if (!ObjectUtils.isEmpty(vo.getFileSize())) {
      entity.setFileSize(vo.getFileSize());
    }
    if (!ObjectUtils.isEmpty(vo.getFilename())) {
      entity.setOriginalFilename(vo.getFilename());
    }
    if (!ObjectUtils.isEmpty(vo.getIsUploaded())) {
      entity.setUploaded(vo.getIsUploaded());
    }

    return entity;
  }

  private StoredFile findByIdOrExcept(UUID id) {
    return storedFileRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당하는 파일이 존재하지 않습니다."));
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public boolean isExist(UUID id) {
    return storedFileRepository.existsById(id);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
  public StoredFile findById(UUID id) {
    return findByIdOrExcept(id);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public StoredFile updateIsUploaded(UUID id) {
    var entity = findByIdOrExcept(id);
    entity.markUploaded();
    return storedFileRepository.save(entity);
  }

}