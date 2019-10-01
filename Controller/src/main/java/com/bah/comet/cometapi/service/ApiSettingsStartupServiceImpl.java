package com.bah.comet.cometapi.service;

import com.bah.comet.cometapi.model.ApiSettings;
import com.bah.comet.cometapi.model.User;
import com.bah.comet.cometapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@EnableConfigurationProperties(ApiSettings.class)
public class ApiSettingsStartupServiceImpl implements ApiSettingsStartupService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApiSettings apiSettings;

    @Autowired
    private UserRepository userRepository;

    /**
     * Executes applicable data updates on application start to align application state with feature flag settings.
     */
    public void alignFeatureFlagData() {
        logger.info("Syncing application state with feature flags...");

        // Personas
        setPersonasDeletedFlag();
    }

    /**
     * Sets or clears the deleted flag for each pre-defined persona based on the api settings.
     */
    private void setPersonasDeletedFlag() {
        if (!apiSettings.getFeatureFlags().isPersonasEnabled()) {
            logger.info("Personas are disabled. Soft deleting the persona users: {}", apiSettings.getPersonas());
            // soft delete the personas we created.
            // in the future, we should have a user type identifier in the database instead of hard coding them.
            Iterable<User> usersToDelete = userRepository.findByEmailIn(apiSettings.getPersonas());
            usersToDelete.forEach((u) -> u.setDeleted(new Date()));
            userRepository.saveAll(usersToDelete);
        }
        else {
            logger.info("Personas are enabled. Clearing the deleted flag for the persona users: {}", apiSettings.getPersonas());
            Iterable<User> usersToDelete = userRepository.findByEmailIn(apiSettings.getPersonas());
            usersToDelete.forEach((u) -> u.setDeleted(null));
            userRepository.saveAll(usersToDelete);
        }
    }
}
