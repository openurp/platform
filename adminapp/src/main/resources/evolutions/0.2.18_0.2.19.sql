create table cfg.credentials (username varchar(100) not null, id int4 not null,
             password varchar(200) not null, name varchar(100) not null, expired_at timestamp not null,
             updated_at timestamp not null, primary key (id));

insert into cfg.credentials (id,name,username,password,updated_at,expired_at)
select next_id('cfg.credentials'),a.username,a.username,a.password,current_timestamp,
current_date+365 from (select distinct username,password from cfg.data_sources )a;

alter table cfg.data_sources add credential_id int4;

update cfg.data_sources a set credential_id=(select c.id from cfg.credentials c
 where c.username=a.username and c.password=a.password);


alter table cfg.data_sources drop column username;
alter table cfg.data_sources drop column password;
