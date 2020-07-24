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

alter table cfg.credentials add domain_id int4;
update cfg.credentials set domain_id=(select min(id) from cfg.domains);
alter table cfg.credentials alter domain_id set not null;

alter table cfg.dbs add domain_id int4;
update cfg.dbs set domain_id=(select min(id) from cfg.domains);
alter table cfg.dbs alter domain_id set not null;

alter table cfg.domains add org_id int4;
update cfg.domains set org_id=(select id from cfg.orgs);
alter table cfg.domains alter org_id set not null;

alter table usr.dimensions add domain_id int4;
update usr.dimensions set domain_id=(select min(id) from cfg.domains);
alter table usr.dimensions alter domain_id set not null;

--alter table usr.groups add org_id int4;
--update usr.groups set org_id=(select id from cfg.orgs);
--alter table usr.groups alter org_id set not null;

alter table usr.password_configs add domain_id int4;
update usr.password_configs set domain_id=(select min(id) from cfg.domains);
alter table usr.password_configs alter domain_id set not null;

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
alter table cfg.credentials add constraint idx_credential unique (domain_id,name);
alter table cfg.dbs add constraint idx_db unique (domain_id,name);
alter table cfg.domains add constraint idx_domain unique (org_id,hostname);
alter table usr.dimensions add constraint idx_dimension_name unique (domain_id,name);
--alter table usr.groups add constraint idx_group unique (org_id,name);
alter table usr.password_configs add constraint idx_password_config unique (domain_id);
alter table usr.user_categories add constraint idx_user_category unique (org_id,name);
alter table usr.users add constraint idx_user_code unique (org_id,code);

create index idx_user_profile on usr.user_profiles(user_id,domain_id);

alter table usr.credentials rename to accounts;
alter table usr.accounts add domain_id int4;
alter table usr.accounts add enabled bool;
alter table usr.accounts add locked bool;
alter table usr.accounts add begin_on date;
alter table usr.accounts add end_on date;
alter table usr.accounts rename expired_on to passwd_expired_on;
alter table usr.accounts rename inactive_on to passwd_inactive_on;

update usr.accounts a set (enabled,locked,begin_on,end_on)=(select u.enabled,u.locked,u.begin_on,u.end_on from usr.users u where u.id=a.user_id);
alter table usr.accounts alter enabled set not null;
alter table usr.accounts alter locked set not null;
alter table usr.accounts alter begin_on set not null;
alter table usr.accounts drop constraint idx_credential_user;
update usr.accounts set domain_id=(select min(id) from cfg.domains);
alter table usr.accounts alter domain_id set not null;
create index idx_account on usr.accounts(user_id,domain_id);

alter table usr.dimensions add value_type bool;
update  usr.dimensions  set value_type =false;
alter table usr.dimensions alter value_type set not null;
alter table usr.dimensions drop column type_name;

alter table usr.users drop column enabled cascade;
alter table usr.users drop column locked cascade;

alter table session.session_infoes add domain_id int4;
update session.session_infoes  set domain_id=(select min(id) from cfg.domains);
alter table session.session_infoes alter domain_id set not null;

alter table session.session_configs add domain_id int4;
update session.session_configs  set domain_id=(select min(id) from cfg.domains);
alter table session.session_configs alter domain_id set not null;
drop index idx_session_config cascade;
alter table session.session_configs add constraint idx_session_config unique(domain_id,category_id);

alter table session.session_events add domain_id int4;
update session.session_events  set domain_id=(select min(id) from cfg.domains);
alter table session.session_events alter domain_id set not null;


