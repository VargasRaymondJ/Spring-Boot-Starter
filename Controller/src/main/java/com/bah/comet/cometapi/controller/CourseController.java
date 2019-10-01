package com.bah.comet.cometapi.controller;

import com.bah.comet.cometapi.model.Course;
import com.bah.comet.cometapi.model.Registration;
import com.bah.comet.cometapi.repository.CourseRepository;
import com.bah.comet.cometapi.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

// TODO: Need to add support for HATEOAS

@CrossOrigin
@RestController
@RequestMapping(value = "/v1/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    // Get a listing of all courses
    // TODO: Need to add filtering capabilities
    @GetMapping
    public Iterable getAllCourses(){
        return courseRepository.findAll();
    }

    //Get information about one course in particular
    @GetMapping(value = "/{id}")
    public Course getCourseInfo(@PathVariable int id) {
        Course courseRetrieved = courseRepository.findById(id);
        courseRetrieved.add(linkTo(methodOn(CourseController.class).getCourseInfo(id)).withSelfRel());
        return courseRetrieved;
    }

    @PostMapping(value = "/user/{userId}/register")
    @PreAuthorize("authentication.userID == #userId or hasRole('ADMIN')")
    public HttpStatus registerForCourses(@PathVariable int userId, @RequestBody List<Course> coursesToEnrollIn){
        coursesToEnrollIn.forEach( course -> { registrationRepository.save(new Registration(userId, course.getCourseId()));
        });
        return HttpStatus.OK;
    }


}
