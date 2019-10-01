package com.bah.comet.cometapi.repository;

import com.bah.comet.cometapi.model.Course;
import org.springframework.data.repository.CrudRepository;
// TODO: Need to add support for pagination
public interface CourseRepository extends CrudRepository<Course, Integer> {
    Course findById(int id);

}
