drop index if exists idx_product_name_trgm;

CREATE INDEX idx_product_name_trgm ON product USING gin (name gin_trgm_ops);