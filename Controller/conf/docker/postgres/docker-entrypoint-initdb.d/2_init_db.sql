\connect comet

-- create roles, initial users, and schema
CREATE ROLE comet_read_only;
CREATE ROLE comet_user;
CREATE ROLE comet_admin;

GRANT comet_read_only TO comet_user;
GRANT comet_user TO comet_admin;
GRANT comet_admin TO dbadmin;

-- create a user for connecting to the db manually
CREATE USER comet_script_user PASSWORD 'user_password' IN ROLE comet_user;

-- reduced-privilege app user (DML operations only)
CREATE USER comet_app PASSWORD 'app_password' IN ROLE comet_user;

-- enable login to comet_admin role so we can use it for database migrations
-- only way to ensure objects created in migration scripts are owned by comet_admin
ALTER ROLE comet_admin LOGIN PASSWORD 'admin_password';

CREATE SCHEMA comet AUTHORIZATION comet_admin;

-- set current and database default search path to include the 'comet' schema
SET search_path = comet,public;
ALTER DATABASE comet SET search_path = comet,public;

GRANT USAGE ON SCHEMA comet TO comet_read_only;
GRANT CREATE ON SCHEMA comet TO comet_admin;

-- setup default privileges for comet_admin users
ALTER DEFAULT PRIVILEGES FOR ROLE comet_admin IN SCHEMA comet GRANT SELECT ON TABLES TO comet_read_only;
ALTER DEFAULT PRIVILEGES FOR ROLE comet_admin IN SCHEMA comet GRANT INSERT, UPDATE, DELETE ON TABLES TO comet_user;

ALTER DEFAULT PRIVILEGES FOR ROLE comet_admin IN SCHEMA comet GRANT SELECT ON SEQUENCES TO comet_read_only;
ALTER DEFAULT PRIVILEGES FOR ROLE comet_admin IN SCHEMA comet GRANT USAGE ON SEQUENCES TO comet_user;

ALTER DEFAULT PRIVILEGES FOR ROLE comet_admin IN SCHEMA comet GRANT EXECUTE ON FUNCTIONS TO comet_user;

-- enable trigram indexing
CREATE EXTENSION pg_trgm SCHEMA public;

-- enable case-insensitive text field
CREATE EXTENSION citext SCHEMA public;
