package com.bah.comet.cometapi.controller;

import com.bah.comet.cometapi.model.ApiSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/settings")
@EnableConfigurationProperties(ApiSettings.class)
public class ApiSettingsController {

    @Autowired
    private ApiSettings settings;

    @GetMapping()
    public ApiSettings getSettings() {
        return settings;
    }
}
