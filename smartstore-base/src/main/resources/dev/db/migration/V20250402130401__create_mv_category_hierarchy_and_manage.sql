drop materialized view if exists mv_category_hierarchy;

create materialized view mv_category_hierarchy as
with recursive category_tree as (
  select
    id,
    name,
    level,
    parent_id,
    array[id] as path
  from category
  where parent_id is null
  and   is_deleted = false

  union all

  select
    c.id,
    c.name,
    c.level,
    c.parent_id,
    ct.path || c.id
  from category c
  join category_tree ct on c.parent_id = ct.id
  where is_deleted = false
)
select
  id,
  name,
  level,
  parent_id,
  path,
  path[1] as lv1_id,
  case when array_length(path, 1) >= 2 then path[2] else null end as lv2_id,
  case when array_length(path, 1) >= 3 then path[3] else null end as lv3_id
from category_tree;

-- 관련 함수가 없을 경우 함수도 함께 정의
create or replace function fn_refresh_mv_category_hierarchy()
returns trigger as $$
begin
  refresh materialized view concurrently mv_category_hierarchy;
  return null;
end;
$$ language plpgsql;

-- 트리거가 이미 존재하면 삭제
drop trigger if exists tg_refresh_mv_category_hierarchy on category;

-- 트리거 생성: category 변경 시 MV 자동 갱신
create trigger tg_refresh_mv_category_hierarchy
after insert or update or delete on category
for each statement
execute procedure fn_refresh_mv_category_hierarchy();