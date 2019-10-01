package com.bah.comet.cometapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This controller exists simply as a quick and easy way to test integration between frontend and backend
@RestController
@RequestMapping(value = "/v1/comet")
public class CometController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Unauthorized endpoint. Setting up for quick integration
    @GetMapping
    public String test(){
        logger.trace("Returning 'hello world'.");
        return "hello world";
    }

    // Authorized endpoint. Requires the "ADMIN" role to access -- Enforced at the Gateway
    @GetMapping(value = "/admin")
    public String authTest(){
        logger.trace("Returning 'hello world admin'.");
        return "hello world admin";
    }

    // example endpoint. We use the PreAuth annotation to ensure that a user's ID matches the variable in the path.
    // This allows us to ensure that a user is only viewing data associated with their unique id. 403 returned otherwise
    @GetMapping(value = "/{id}/data")
    @PreAuthorize("authentication.userID == #id")
    public String myTest(@PathVariable int id){
        return "You can only view *your* data!";
    }


}
