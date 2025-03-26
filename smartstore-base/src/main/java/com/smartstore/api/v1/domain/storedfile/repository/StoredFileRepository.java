package com.smartstore.api.v1.domain.storedfile.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartstore.api.v1.domain.storedfile.entity.StoredFile;

public interface StoredFileRepository extends JpaRepository<StoredFile, UUID> {
  List<StoredFile> findAllByIdIn(List<UUID> ids);
}
