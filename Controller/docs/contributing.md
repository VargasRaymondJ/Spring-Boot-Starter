# Contributing to the GSA Comet Technical Challenge API Code Base

## Getting Started
This doc covers contributing guidelines specific to the api project. Please refer to the [Code Base Contributing Guidelines](https://github.com/boozallen/gsa-comet-dry-run-3-ui/blob/master/docs/contributing.md) for general guidelines and processes.

## REST Practices
Always strive to follow all of the [REST API Best Practices](https://swagger.io/blog/api-design/api-design-best-practices/).
* Use plural nouns for urls
* Describe resource functionality with HTTP methods
* Utilize the appropriate HTTP response codes
* GET methods should not alter the state
* Use sub resources for relations (ie. `/cars/711/drivers`
* Always follow the Principle of Least Privilege when returning data in a response payload.

### Swagger Docs
All new or updated models and endpoints need to be documented with [Swagger annotations](https://github.com/swagger-api/swagger-core/wiki/annotations).
At a minimum, use the `@ApiOperation`, `@ApiModel`, and `@ApiResponses` annotations to document the purposes of the
endpoint and the details of the input and output.

## Logging
Logging should be implemented with security as the top priority. The secondary priority is production support. Logging serves the purpose of aiding the development team in resolving issues, monitoring user actions and performance, and helping to identify usage patterns.

Some best practices for logging include:
* NEVER log credentials or other business sensitive information.
* Allow logging level configuration and default to a minimum level of WARN in production environments. When applicable, allow for more granular logging at the user level for targeted debugging.
* Following the project's [logging level standards](#logging-level-standards)
* Requesting ip addresses should be logged with the route details, including the sanitized payload at the debug level
* All business critical actions should include a starting and completion log statement, with every exit of the function covered with a statement.

### Logging Level Standards
* trace : for fine detailed troubleshooting information (ie. timing/performance metrics)
* debug : for debug information
* info : for informative status of the application (success, ...)
* warning : for non-critical errors that do not prevent normal application behavior
* error : for critical errors that prevent normal application behavior


## SQL
### Adding Migrations
When adding a new migration, these guidelines should be followed to keep security and consistency top priorities.

* For new tables, add the `LIKE template INCLUDING ALL` notation after the primary key. Use the `LIKE template_authenticated` when the table data is likely to be updated/inserted/deleted by an authenticated user.
The template simply adds a predefined column to your table definition to help capture the last updated timestamp and possibly the user making any updates.
* Add a corresponding audit table. The reasoning is outlined in the [auditing with triggers](#automatic-auditing-with-triggers) section.
    ```
    CREATE TABLE <table_name>_audit(
      aud_id SERIAL PRIMARY KEY,
      aud_operation CHAR(1),
      last_change_ts TIMESTAMP NOT NULL DEFAULT current_timestamp,
      last_change_by character varying(255),
      LIKE app_user EXCLUDING ALL
    );

    REVOKE UPDATE, DELETE, TRUNCATE ON <table_name>_audit FROM comet_user, comet_admin;

    CREATE TRIGGER app_<table_name>
    AFTER INSERT OR UPDATE OR DELETE ON app_user
    FOR EACH ROW EXECUTE PROCEDURE data_change_audit();

    ```
* Naming conventions
    * Migration file names should follow the format `VX__description.sql` (ie. `V1__backfill_user_created_date.sql`)
    * Singular table names
    * Use underscores in names instead of camel case or dashes
* Avoid downtime deployments - do not drop columns, alter past migrations, add new required columns without back-filling data, etc.
* Include a commented out rollback block of code to undo your changes if a rollback is necessary.

### Auditing
#### Automatic Auditing with Triggers
For each new table, you must create a corresponding audit table and triggers. This does a few things:
* Captures all updates/inserts/deletes, which allows us to view a precise change log of data.
* Captures the db user that made the updates for debugging and tracking.
* Captures the date of changes instead of relying on manual updates via code.
* Removes the ability to delete this audit data from the app user and the migration user to keep this data sacred.

#### Manual Auditing
When user actions should be captured, utilize the `template_authenticated` template for the table creation and manually set the `app_user_last_changed` field to the app request context's authenticated user id.
This allows us to easily see which logged in user altered the data. Tracking this depends solely on the developer and must be set manually in the code.

### Performance
All queries created with an ORM should be logged as plain sql and analyzed with an execution plan. To do so, run `SELECT EXPLAIN ANALYZE <your query>` in psql or a db editor.
You can analyze this directly or use an online tool such as [Tatiyants](https://tatiyants.com), which keeps all information local.

#### General Guidelines
* Add indexes to foreign key references - Postgres does not automatically add indexes for foreign keys. You have to add them manually.
* Add indexes for queries that will be run frequently.
* Remember that the index usage changes as more data is added.
* Keep the data normalized.
* Keep privacy a top priority - encrypt passwords, don't export production data, limit even read access to production data.

## Comments
Every method of every file of custom code must include a JavaDoc comment `/** ... */`. This comment must:
* Explain what the method does.
* Provide any useful business domain knowledge for the method's purpose or actions.
* Include a description of each input parameter.
* Include a description of the output.

## Tests
Always aim for 100% code coverage, but make useful tests the top priority. Focus on business logic, error responses, data security, and the altering of business-critical data.
Utilize mocking frameworks to isolate the unit you are testing.
