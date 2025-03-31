package com.smartstore.api.v1.domain.category.projection;

import java.util.List;
import java.util.UUID;

public record CategoryL1Projection(
    UUID id,
    String name,
    Integer orderBy,
    List<CategoryL2Projection> children) {
}
