-- carefully
alter table usr.avatars drop column image;
drop table bulletin.attachments;
alter table bulletin.docs drop column file_id;
