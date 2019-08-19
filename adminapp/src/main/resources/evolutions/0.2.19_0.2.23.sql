create table cfg.dbs_properties (db_id int4 not null, value varchar(255) not null, name varchar(255) not null, primary key (db_id, value, name));
alter table cfg.data_sources alter column max_active rename to maximum_pool_size;

