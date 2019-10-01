CREATE TABLE IF NOT EXISTS app_user
(
  id SERIAL PRIMARY KEY,
  LIKE template_authenticated INCLUDING ALL,
  email character varying(255),
  password character varying(255),
  first_name character varying(255),
  last_name character varying(255),
  roles text,
  created_on TIMESTAMP default current_timestamp,
  last_login TIMESTAMP default current_timestamp
);

CREATE TABLE IF NOT EXISTS app_user_audit(
  aud_id SERIAL PRIMARY KEY,
  aud_operation CHAR(1),
  last_change_ts TIMESTAMP NOT NULL DEFAULT current_timestamp,
  last_change_by character varying(255),
  LIKE app_user EXCLUDING ALL
);

REVOKE UPDATE, DELETE, TRUNCATE ON app_user_audit FROM comet_user, comet_admin;

CREATE TRIGGER app_user_audit
AFTER INSERT OR UPDATE OR DELETE ON app_user
FOR EACH ROW EXECUTE PROCEDURE data_change_audit();

/* rollback
DROP TRIGGER IF EXISTS app_user_audit;
DROP TABLE IF EXISTS app_user_audit;
DROP TABLE IF EXISTS app_user;
*/
