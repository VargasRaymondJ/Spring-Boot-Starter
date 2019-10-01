CREATE TABLE comet.course_catalog
(
  id SERIAL PRIMARY KEY,
  class_title varchar(255),
  class_description text,
  instructor_first_name varchar(255),
  instructor_last_name varchar(255),
  subject varchar(55),
  credits int
);