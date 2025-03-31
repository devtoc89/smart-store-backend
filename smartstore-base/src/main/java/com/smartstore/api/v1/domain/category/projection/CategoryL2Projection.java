package com.smartstore.api.v1.domain.category.projection;

import java.util.List;
import java.util.UUID;

public record CategoryL2Projection(
    UUID id,
    String name,
    Integer orderBy,
    List<CategoryNodeProjection> children) {
}