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
drop constraint if exists UK7rtwltrxc00rckamowbbl2m11;

alter table if exists admin_tokens add constraint UK7rtwltrxc00rckamowbbl2m11 unique (refresh_token);

alter table if exists admin_tokens
drop constraint if exists UKa167bxqo1h59kljm439nrysba;

alter table if exists admin_tokens add constraint UKa167bxqo1h59kljm439nrysba unique (admin_id);

alter table if exists admins
drop constraint if exists UK47bvqemyk6vlm0w7crc3opdd4;

alter table if exists admins add constraint UK47bvqemyk6vlm0w7crc3opdd4 unique (email);

alter table if exists admin_tokens add constraint FK582rus899qpvcddc5xsgbka8u foreign key (admin_id) references admins;

alter table if exists category_l2 add constraint FKkj6a2ow2f69twddjnscm22pwl foreign key (category_l1_id) references category_l1;

alter table if exists category_node add constraint FK6n6ahmb8pgus5i9m6c4buhclp foreign key (category_l2_id) references category_l2;

alter table if exists product_image add constraint FKa12syfcxrc632xrg98i8vwk5p foreign key (file_id) references file;

alter table if exists product_image add constraint FK6oo0cvcdtb6qmwsga468uuukk foreign key (product_id) references product;
