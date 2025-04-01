CREATE INDEX idx_category_l2_active ON category_l2 (category_l1_id)
WHERE
  is_deleted IS DISTINCT
FROM
  true;

CREATE INDEX idx_category_node_active ON category_node (category_l2_id)
WHERE
  is_deleted IS DISTINCT
FROM
  true;
