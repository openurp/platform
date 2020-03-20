create table usr.password_configs (mindays integer not null, minclass integer not null, usercheck boolean not null, lcredit integer not null, maxdays integer not null, id integer not null, ucredit integer not null, idledays integer not null, ocredit integer not null, warnage integer not null, minlen integer not null, dcredit integer not null, maxlen integer not null);
alter table usr.password_configs add constraint pk_ktglwm779su3bvsmvdgpueox8 primary key (id);
create table usr.credentials (inactive_on date not null, updated_at timestamp not null, id bigint not null, expired_on date not null, user_id bigint not null, password varchar(200) not null);
alter table usr.credentials add constraint pk_onen3x0wxu60l38jaqwd1230k primary key (id);
alter table usr.credentials add constraint idx_credential_user unique (user_id);
insert into usr.credentials(id,user_id,password,updated_at,expired_on,inactive_on)
select datetime_id(),u.id,u.password,coalesce(u.updated_at,now()),current_date+180,current_date+200 from usr.users u;
alter table usr.users alter password drop not null;
