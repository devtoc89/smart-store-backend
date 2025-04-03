drop table if exists category;

create table category (
  id uuid not null primary key,
  name varchar(130) not null,
  level int not null check (level in (1, 2, 3)),
  order_by int not null,
  parent_id uuid,
  is_deleted boolean not null default false,
  created_at timestamp
  with
    time zone,
    updated_at timestamp
  with
    time zone,
    deleted_at timestamp(6)
  with
    time zone,
    foreign key (parent_id) references category (id)
);

-- 1. Lv1 (category_l1 → category)
insert into
  category (
    id,
    name,
    level,
    order_by,
    parent_id,
    is_deleted,
    created_at,
    updated_at,
    deleted_at
  )
select
  id,
  name,
  1 as level,
  order_by,
  null as parent_id,
  is_deleted,
  created_at,
  updated_at,
  deleted_at
from
  category_l1;

-- 2. Lv2 (category_l2 → category)
insert into
  category (
    id,
    name,
    level,
    order_by,
    parent_id,
    is_deleted,
    created_at,
    updated_at,
    deleted_at
  )
select
  l2.id,
  l2.name,
  2 as level,
  order_by,
  l2.category_l1_id as parent_id,
  l2.is_deleted,
  l2.created_at,
  l2.updated_at,
  l2.deleted_at
from
  category_l2 l2;

-- 3. Lv3 (category_node → category)
insert into
  category (
    id,
    name,
    level,
    order_by,
    parent_id,
    is_deleted,
    created_at,
    updated_at,
    deleted_at
  )
select
  n.id,
  n.name,
  3 as level,
  order_by,
  n.category_l2_id as parent_id,
  n.is_deleted,
  n.created_at,
  n.updated_at,
  n.deleted_at
from
  category_node n;

drop index if exists idx_category_l2_active;

drop index if exists idx_category_node_active;

drop table if exists category_node;

drop table if exists category_l2;

drop table if exists category_l1;
