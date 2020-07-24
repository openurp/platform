create table bulletin.docs_user_categories (doc_id int8 not null, user_category_id int4 not null, primary key (doc_id, user_category_id));
create table bulletin.notices_user_categories (notice_id int8 not null, user_category_id int4 not null, primary key (notice_id, user_category_id));
create index idx_doc_user_cateogry_doc on bulletin.docs_user_categories(doc_id);
create index idx_notice_user_cateogry_notice on bulletin.notices_user_categories(notice_id);

insert into bulletin.docs_user_categories(doc_id,user_category_id) select id,user_category_id from bulletin.docs;
insert into bulletin.notices_user_categories(notice_id,user_category_id) select id,user_category_id from bulletin.notices;

alter table bulletin.docs drop column user_category_id;
alter table bulletin.notices drop column user_category_id;
