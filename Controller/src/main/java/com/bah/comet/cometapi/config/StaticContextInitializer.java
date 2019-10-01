package com.bah.comet.cometapi.config;

import com.bah.comet.cometapi.model.ApiSettings;
import com.bah.comet.cometapi.model.PageRequest;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class StaticContextInitializer {

    @Autowired
    private ApiSettings settings;

    /**
     * This initializer helps set our api settings within static classes.
     */
    @PostConstruct
    public void init() {
        PageRequest.setApiSettings(settings);
    }
}
