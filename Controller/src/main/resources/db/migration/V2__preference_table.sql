CREATE TABLE comet.preferences
(
  id bigint NOT NULL PRIMARY KEY,
  email varchar(255),
  user_id INTEGER,
  name varchar(255),
  type varchar(55),
  date_min varchar(25),
  date_max varchar(25),
  distance varchar(55),
  size varchar(55),
  FOREIGN KEY (user_id) REFERENCES app_user(id)
);