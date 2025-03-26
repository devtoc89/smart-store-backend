package com.smartstore.api.v1.domain.storedfile.vo;

import lombok.Builder;

@Builder
public record StoredPresignedFileDTO(String key, String url, String fileId) {
}