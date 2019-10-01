package com.bah.comet.cometapi.repository;

import com.bah.comet.cometapi.model.Registration;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistrationRepository extends CrudRepository<Registration, Integer> {

    List<Registration> findByUserId(int userId);
}
