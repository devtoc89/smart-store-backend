drop index if exists idx_mv_category_hierarchy_lv1_id_lv2_id_lv3_id;

CREATE UNIQUE INDEX idx_mv_category_hierarchy_lv1_id_lv2_id_lv3_id ON mv_category_hierarchy (lv1_id, lv2_id, lv3_id, id);