package com.bah.comet.cometapi.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.settings.feature-flags")
public class FeatureFlags {
    // Ability to login as a persona without using real credentials.
    private boolean personasEnabled;

    public boolean isPersonasEnabled() {
        return personasEnabled;
    }

    public void setPersonasEnabled(boolean personasEnabled) {
        this.personasEnabled = personasEnabled;
    }
}
