/* Create the template table defining required fields */
CREATE TABLE template (
  orig_insert_ts TIMESTAMP NOT NULL DEFAULT current_timestamp
);

CREATE TABLE template_authenticated (
    LIKE template INCLUDING ALL,
    app_user_last_changed INTEGER
);

/* Trigger function to add a row to the appropriate audit table for any table data change. */

CREATE OR REPLACE FUNCTION data_change_audit() RETURNS TRIGGER AS $data_change_audit$
DECLARE
  audit_table_name TEXT;
  audit_id_seq TEXT;
BEGIN
  audit_table_name := TG_TABLE_NAME || '_audit';
  audit_id_seq := audit_table_name || '_aud_id_seq';
  IF (TG_OP = 'DELETE') THEN
    EXECUTE format('INSERT INTO %I SELECT nextval(%L::regclass), ''D'', current_timestamp, current_user, $1.*', audit_table_name, audit_id_seq) USING OLD;
    RETURN OLD;
  ELSIF (TG_OP = 'UPDATE') THEN
    EXECUTE format('INSERT INTO %I SELECT nextval(%L::regclass), ''U'', current_timestamp, current_user, $1.*', audit_table_name, audit_id_seq) USING NEW;
    RETURN NEW;
  ELSIF (TG_OP = 'INSERT') THEN
    EXECUTE format('INSERT INTO %I SELECT nextval(%L::regclass), ''I'', current_timestamp, current_user, $1.*', audit_table_name, audit_id_seq) USING NEW;
    RETURN NEW;
  END IF;
  RETURN NULL;
END;
$data_change_audit$ LANGUAGE plpgsql;


/* rollback
DROP FUNCTION IF EXISTS data_change_audit();
*/