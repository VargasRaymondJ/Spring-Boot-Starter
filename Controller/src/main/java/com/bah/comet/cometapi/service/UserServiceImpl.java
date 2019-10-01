package com.bah.comet.cometapi.service;

import com.bah.comet.cometapi.controller.UserController;
import com.bah.comet.cometapi.exception.GenericHttpException;
import com.bah.comet.cometapi.model.PageRequest;
import com.bah.comet.cometapi.model.User;
import com.bah.comet.cometapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private UserRepository userRepository;

    //Password Encoder to handle hashing and comparison of passwords
    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public User getUserInformation(int id) {

        logger.debug("User info requested for User with ID {}", id);
        User user = userRepository.findById(id).get();
        Link selfLink = linkTo(methodOn(UserController.class)
                .getUserInfo(id)).withSelfRel();
        user.add(selfLink);

        return user;
    }

    @Override
    public Iterable getAllUsers(Optional<Integer> page, Optional<Integer> size) {
        // Create a pagination object.
        Pageable pageDetails = PageRequest.create(page, size);
        logger.debug("Paginating results: {}", pageDetails);

        // Retrieve all users in the DB
        Page<User> userList = userRepository.findAll(pageDetails);

        // Add HATEOAS links to users
        for (User u : userList) {
            u.add(linkTo(methodOn(UserController.class)
                    .getUserInfo(u.getUserId())).withSelfRel());
        }

        return userList;
    }

    @Override
    public void createUser(User newUser) throws GenericHttpException {

        logger.debug("Attempting to create a new user");

        //  Not going to allow roles get set during the creation of a new user -- anyone could get themselves access
        if (!StringUtils.isEmpty(newUser.getRoles()))
            throw new GenericHttpException("Roles cannot be defined during user registration", HttpStatus.BAD_REQUEST);

        // Email and Username are required fields, so we make sure those fields aren't empty
        if (StringUtils.isEmpty(newUser.getEmail()) || StringUtils.isEmpty(newUser.getPassword()))
            throw new GenericHttpException("One or more necessary fields missing", HttpStatus.BAD_REQUEST);

        // Ensure that a user with that email address doesn't already exist
        if (userRepository.findByEmailIgnoreCaseAndDeletedIsNull(newUser.getEmail()).isPresent())
            throw new GenericHttpException("User Already Exists", HttpStatus.BAD_REQUEST);

        // Hash the password
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setLastLogin(new Date());

        // Assign USER role
        newUser.setRoles("ROLE_USER");

        // Save the user to the DB
        userRepository.save(newUser);
    }

    @Override
    public void changePassword(User payload) throws GenericHttpException {
        // Grab the User from the DB
        User existingUser = userRepository.findByEmailIgnoreCaseAndDeletedIsNull(payload.getEmail())
                .orElseThrow(() -> new GenericHttpException("User Not Found", HttpStatus.NOT_FOUND));

        // Bad request if the passwords don't match
        if (!encoder.matches(payload.getPassword(), existingUser.getPassword()))
            throw new GenericHttpException("Incorrect Password", HttpStatus.UNAUTHORIZED);

        // Set the new password
        existingUser.setPassword(encoder.encode(payload.getNewPassword()));

        // Update record in the database
        userRepository.save(existingUser);
    }

}
