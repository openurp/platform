create schema blob;
create table blob.blob_metas(id bigint,owner varchar(100) not null,name varchar(300)  not null,
                              size bigint  not null,sha varchar(100)  not null,media_type varchar(60)  not null,
                              profile_id int4 not null,path varchar(400) not null,updated_at timestamp not null);
alter table blob.blob_metas add primary key (id);
create unique  index idx_blob_meta_profile_path on blob.blob_metas (profile_id,path);
create view blob.users as select name,secret as key from cfg.apps;
create table blob.profiles(id int4 primary key,name varchar(100) not null,path varchar(100) not null,users varchar(200),
                           named_by_sha bool not null,public_list bool not null,
                           public_download bool not null);
insert into blob.profiles(id,name,path,users,named_by_sha,public_list,public_download)
values(1,'系统管理','/platform','platform-adminapp,platform-ws,platform-userapp',true,false,true);

alter table usr.avatars add path varchar(200);
alter table bulletin.docs add path varchar(200);
alter table bulletin.docs add size int4;

update  bulletin.docs d set size=(select a.size from bulletin.attachments a where a.id=d.file_id);
