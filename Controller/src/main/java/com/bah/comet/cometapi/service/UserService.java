package com.bah.comet.cometapi.service;

import com.bah.comet.cometapi.exception.GenericHttpException;
import com.bah.comet.cometapi.model.User;

import java.util.Optional;

public interface UserService {
    User getUserInformation(int id);
    Iterable getAllUsers(Optional<Integer> page, Optional<Integer> size);
    void createUser(User newUser) throws GenericHttpException;
    void changePassword(User payload) throws GenericHttpException;

}
