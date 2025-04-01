create table admin_tokens (
  id uuid not null,
  expires_at timestamp(6)
  with
    time zone not null,
    refresh_token varchar(512) not null,
    admin_id uuid not null,
    primary key (id)
);

create table admins (
  id uuid not null,
  created_at timestamp(6)
  with
    time zone,
    deleted_at timestamp(6)
  with
    time zone,
    is_deleted boolean not null,
    updated_at timestamp(6)
  with
    time zone,
    email varchar(255) not null,
    is_activated boolean not null,
    last_login_at timestamp(6)
  with
    time zone,
    login_fail_count integer not null,
    nickname varchar(255) not null,
    password varchar(100) not null,
    role varchar(255) not null check (role in ('SUPER_ADMIN', 'ADMIN', 'USER')),
    primary key (id)
);

create table category_l1 (
  id uuid not null,
  created_at timestamp(6)
  with
    time zone,
    deleted_at timestamp(6)
  with
    time zone,
    is_deleted boolean not null,
    updated_at timestamp(6)
  with
    time zone,
    name varchar(255) not null,
    order_by integer not null,
    primary key (id)
);

create table category_l2 (
  id uuid not null,
  created_at timestamp(6)
  with
    time zone,
    deleted_at timestamp(6)
  with
    time zone,
    is_deleted boolean not null,
    updated_at timestamp(6)
  with
    time zone,
    name varchar(255) not null,
    order_by integer not null,
    category_l1_id uuid not null,
    primary key (id)
);

create table category_node (
  id uuid not null,
  created_at timestamp(6)
  with
    time zone,
    deleted_at timestamp(6)
  with
    time zone,
    is_deleted boolean not null,
    updated_at timestamp(6)
  with
    time zone,
    name varchar(255) not null,
    order_by integer not null,
    category_l2_id uuid not null,
    primary key (id)
);

create table file (
  id uuid not null,
  created_at timestamp(6)
  with
    time zone,
    deleted_at timestamp(6)
  with
    time zone,
    is_deleted boolean not null,
    updated_at timestamp(6)
  with
    time zone,
    content_type varchar(100),
    file_size bigint,
    is_uploaded boolean not null,
    key varchar(512) not null,
    original_filename varchar(255),
    primary key (id)
);

create table product (
  id uuid not null,
  created_at timestamp(6)
  with
    time zone,
    deleted_at timestamp(6)
  with
    time zone,
    is_deleted boolean not null,
    updated_at timestamp(6)
  with
    time zone,
    category_id uuid not null,
    description varchar(255) not null,
    name varchar(255) not null,
    price integer not null,
    primary key (id)
);

create table product_image (
  id uuid not null,
  created_at timestamp(6)
  with
    time zone,
    deleted_at timestamp(6)
  with
    time zone,
    is_deleted boolean not null,
    updated_at timestamp(6)
  with
    time zone,
    is_main boolean not null,
    order_by integer not null,
    product_id uuid,
    file_id uuid not null,
    primary key (id)
);

alter table if exists admin_tokens
drop constraint if exists uk_admin_tokens_refresh_token;

alter table if exists admin_tokens add constraint uk_admin_tokens_refresh_token unique (refresh_token);

alter table if exists admin_tokens
drop constraint if exists uk_admin_tokens_admin_id;

alter table if exists admin_tokens add constraint uk_admin_tokens_admin_id unique (admin_id);

alter table if exists admins
drop constraint if exists uk_admins_email;

alter table if exists admins add constraint uk_admins_email unique (email);

alter table if exists admin_tokens add constraint fk_admin_tokens_admins_admin_id foreign key (admin_id) references admins;

alter table if exists category_l2 add constraint fk_category_l2_category_l1_id foreign key (category_l1_id) references category_l1;

alter table if exists category_node add constraint fk_category_node_category_l2_id foreign key (category_l2_id) references category_l2;

alter table if exists product_image add constraint fk_product_image_file_id foreign key (file_id) references file;

alter table if exists product_image add constraint fk_product_image_product_id foreign key (product_id) references product;
