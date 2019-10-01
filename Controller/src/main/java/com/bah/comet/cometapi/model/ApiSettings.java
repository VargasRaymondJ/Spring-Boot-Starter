package com.bah.comet.cometapi.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

@ConfigurationProperties("app.settings")
@EnableConfigurationProperties(FeatureFlags.class)
public class ApiSettings {

    // List of application feature flag settings
    @Autowired
    private FeatureFlags featureFlags;

    // List of persona identifiers.
    @Value("#{'${app.settings.personas}'.split(',')}")
    private List<String> personas;

    private Integer defaultPaginationSize;

    private Integer maxPaginationSize;

    public FeatureFlags getFeatureFlags() {
        return featureFlags;
    }

    public void setFeatureFlags(FeatureFlags featureFlags) {
        this.featureFlags = featureFlags;
    }

    public List<String> getPersonas() {
        return personas;
    }

    public void setPersonas(List<String> personas) {
        this.personas = personas;
    }

    public Integer getDefaultPaginationSize() {
        return defaultPaginationSize;
    }

    public void setDefaultPaginationSize(Integer defaultPaginationSize) {
        this.defaultPaginationSize = defaultPaginationSize;
    }

    public Integer getMaxPaginationSize() {
        return maxPaginationSize;
    }

    public void setMaxPaginationSize(Integer maxPaginationSize) {
        this.maxPaginationSize = maxPaginationSize;
    }
}
