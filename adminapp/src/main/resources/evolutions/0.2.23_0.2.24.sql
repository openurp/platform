create table bulletin.sensitive_words (id int4 not null, content varchar(30) not null, primary key (id));
alter table bulletin.notices add auditor_id bigint;
alter table bulletin.notices add status int4;
alter table bulletin.notices add created_at timestamp;
alter table bulletin.notices add updated_at timestamp;
alter table bulletin.notices add published_at timestamp;
update bulletin.notices set published_at = published_on;
update bulletin.notices set created_at=published_on;
update bulletin.notices set updated_at=published_on;
alter table bulletin.notices drop column published_on;

update bulletin.notices set status=3;
alter table bulletin.notices alter status  set not null;
