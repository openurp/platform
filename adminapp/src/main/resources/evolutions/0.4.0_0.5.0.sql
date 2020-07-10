alter table cfg.domains rename to app_groups;
alter table cfg.apps rename domain_id to group_id;
alter table cfg.apps add domain_id int4;

create table cfg.domains(id int4 ,name varchar(100),title varchar(100),hostname varchar(100));
insert into cfg.domains(id,name,title,hostname) select id,name,title,'xx.xx.edu.cn' from cfg.app_groups where parent_id is null;
alter table cfg.app_groups  rename parent_id to domain_id;
delete from cfg.app_groups  where domain_id is null;

alter table cfg.app_groups drop constraint pk_qmiqopkrlr3yjian3tuafihea;
alter table cfg.app_groups add constraint pk_kya1tu2eyjl7ubus7ik809f1o primary key (id);
alter table cfg.domains add constraint pk_qmiqopkrlr3yjian3tuafihea primary key (id);
update cfg.apps set domain_id = (select d.id from cfg.domains d);

alter table usr.user_profiles add name varchar(200);
update usr.user_profiles set name='默认';

alter table cfg.credentials add org_id int4;
update cfg.credentials set org_id=(select id from cfg.orgs);
alter table cfg.credentials alter org_id set not null;

alter table cfg.dbs add org_id int4;
update cfg.dbs set org_id=(select id from cfg.orgs);
alter table cfg.dbs alter org_id set not null;

alter table cfg.domains add org_id int4;
update cfg.domains set org_id=(select id from cfg.orgs);
alter table cfg.domains alter org_id set not null;

alter table usr.dimensions add org_id int4;
update usr.dimensions set org_id=(select id from cfg.orgs);
alter table usr.dimensions alter org_id set not null;

--alter table usr.groups add org_id int4;
--update usr.groups set org_id=(select id from cfg.orgs);
--alter table usr.groups alter org_id set not null;

alter table usr.password_configs add org_id int4;
update usr.password_configs set org_id=(select id from cfg.orgs);
alter table usr.password_configs alter org_id set not null;

alter table usr.roles add domain_id int4;
update usr.roles set domain_id=(select min(id) from cfg.domains);
alter table usr.roles alter domain_id set not null;

alter table usr.users add org_id int4;
update usr.users set org_id=(select id from cfg.orgs);
alter table usr.users alter org_id set not null;

alter table usr.user_categories add org_id int4;
update usr.user_categories set org_id=(select id from cfg.orgs);
alter table usr.user_categories alter org_id set not null;

alter table cfg.app_groups add constraint idx_app_group unique (domain_id,name);
alter table cfg.apps add constraint idx_app unique (domain_id,name);
alter table usr.roles add constraint idx_role_name unique (domain_id,name);
alter table cfg.credentials add constraint idx_credential unique (org_id,name);
alter table cfg.dbs add constraint idx_db unique (org_id,name);
alter table cfg.domains add constraint idx_domain unique (org_id,hostname);
alter table usr.dimensions add constraint idx_dimension_name unique (org_id,name);
--alter table usr.groups add constraint idx_group unique (org_id,name);
alter table usr.password_configs add constraint idx_password_config unique (org_id);
alter table usr.user_categories add constraint idx_user_category unique (org_id,name);
alter table usr.users add constraint idx_user_code unique (org_id,code);
