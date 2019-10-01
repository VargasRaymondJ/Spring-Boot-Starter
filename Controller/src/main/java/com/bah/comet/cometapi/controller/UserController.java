package com.bah.comet.cometapi.controller;


import com.bah.comet.cometapi.exception.GenericHttpException;
import com.bah.comet.cometapi.model.Course;
import com.bah.comet.cometapi.model.Registration;
import com.bah.comet.cometapi.model.PageRequest;
import com.bah.comet.cometapi.model.User;
import com.bah.comet.cometapi.repository.CourseRepository;
import com.bah.comet.cometapi.repository.RegistrationRepository;
import com.bah.comet.cometapi.repository.UserRepository;
import com.bah.comet.cometapi.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/users")
@Api(value = "/users", description = "Operations about users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CourseRepository courseRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    // Return information on a specific user
    @GetMapping(value = "/{id}")
    @PreAuthorize("authentication.userID == #id")
    public User getUserInfo(@PathVariable int id) {
        return userService.getUserInformation(id);
    }


    @GetMapping()
    @ApiOperation(value = "Gets a list of users",
            notes = "Must be authenticated as an administrator",
            response = User.class,
            responseContainer = "List")
    @PreAuthorize("hasRole('Admin')")
    public Iterable getAllUsers(@ApiParam(name = "page", value = "Page number", required = false) @RequestParam(required = false) Optional<Integer> page,
                                @ApiParam(name = "size", value = "Page number", required = false) @RequestParam(required = false) Optional<Integer> size) {

        return userService.getAllUsers(page,size);
    }

    @PostMapping(value = "/new")
    @ApiOperation(value = "Creates a new user",
            notes = "Defaults to the user role, email must be unique",
            response = User.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Roles field must be blank"),
            @ApiResponse(code = 400, message = "One of more necessary fields missing"),
            @ApiResponse(code = 400, message = "User already exists")
    })
    public HttpStatus createUser(@ApiParam(name = "newUser", value = "User to be created", required = true) @RequestBody User newUser) throws GenericHttpException {

        userService.createUser(newUser);
        return HttpStatus.OK;
    }

    @PatchMapping(value = "/changePassword")
    public HttpStatus changePassword(@RequestBody User payload) throws GenericHttpException {

        userService.changePassword(payload);
        return HttpStatus.OK;
    }

    // Return all of the courses a user has registered for
    @GetMapping(value = "/{userId}/courses")
    @PreAuthorize("authentication.userID == #userId")
    public List getUserCourses(@PathVariable int userId) {

        List registeredCourses = new ArrayList<Course>();
        List<Registration> registeredList = registrationRepository.findByUserId(userId);
        registeredList.forEach(r -> registeredCourses.add(courseRepository.findById(r.getCourseId())));

        return registeredCourses;

    }
}
