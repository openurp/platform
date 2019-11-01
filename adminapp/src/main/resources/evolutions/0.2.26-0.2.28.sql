alter table session.session_infoes add tti_minutes int4;
alter table session.session_infoes add category_id int4;
create index idx_session_info_category on session.session_infoes (category_id);

CREATE OR REPLACE FUNCTION add_minutes(timestamp without time zone, integer)
RETURNS timestamp  without time zone
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
AS $BODY$
begin
  return $1 +  $2 * (interval '1' minute);
end;
$BODY$;

create table session.session_configs (concurrent int4 not null, tti_minutes int4 not null,capacity int4 not null, check_concurrent boolean not null, id int8 not null, category_id int4 not null, check_capacity boolean not null, primary key (id));
create table session.session_events (username varchar(100) not null, id int8 not null, principal varchar(100) not null, detail varchar(1000), event_type int4 not null, name varchar(100) not null, ip varchar(255) not null, updated_at timestamp not null, primary key (id));

create unique index idx_session_config on  session.session_configs(category_id);
