delete from session.session_infoes;
alter table session.session_infoes rename tti_minutes  to tti_seconds;

CREATE OR REPLACE FUNCTION add_seconds(timestamp without time zone, integer)
RETURNS timestamp  without time zone
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE
AS $BODY$
begin
  return $1 +  $2 * (interval '1' second);
end;
$BODY$;

drop FUNCTION add_minutes;
