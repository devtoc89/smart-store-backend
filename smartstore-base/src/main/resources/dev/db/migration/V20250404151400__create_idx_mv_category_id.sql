drop index if exists idx_mv_category_hierarchy_id;

CREATE UNIQUE INDEX idx_mv_category_hierarchy_id ON mv_category_hierarchy (id);