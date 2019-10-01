package com.bah.comet.cometapi.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.proxy")
public class ProxyProperties {

    // Third party api url
    private String url;

    // Authorization header
    private String authorization;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorizationHeader) {
        this.authorization = authorizationHeader;
    }
}
