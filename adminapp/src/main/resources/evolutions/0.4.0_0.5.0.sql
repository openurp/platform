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
