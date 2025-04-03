drop index if exists idx_category_name_trgm;

CREATE INDEX idx_category_name_trgm ON category USING gin (name gin_trgm_ops);