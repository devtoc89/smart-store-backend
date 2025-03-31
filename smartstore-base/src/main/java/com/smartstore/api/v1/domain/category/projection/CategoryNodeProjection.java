package com.smartstore.api.v1.domain.category.projection;

import java.util.UUID;

public record CategoryNodeProjection(
    UUID id,
    String name,
    Integer orderBy) {
}