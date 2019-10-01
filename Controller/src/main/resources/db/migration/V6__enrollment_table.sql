CREATE TABLE IF NOT EXISTS course_registration
(
  registration_id SERIAL PRIMARY KEY,
  user_id int,
  course_id int,
  FOREIGN KEY (user_id) REFERENCES app_user(id),
  FOREIGN KEY (course_id) REFERENCES course_catalog(id)
);