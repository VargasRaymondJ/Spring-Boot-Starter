ALTER TABLE app_user ADD COLUMN deleted TIMESTAMP;
ALTER TABLE app_user_audit ADD COLUMN deleted TIMESTAMP;

/**
rollback
ALTER TABLE app_user DROP COLUMN deleted;
ALTER TABLE app_user_audit DROP COLUMN deleted;
*/