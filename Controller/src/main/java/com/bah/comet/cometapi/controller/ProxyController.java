package com.bah.comet.cometapi.controller;

import com.bah.comet.cometapi.model.ProxyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity; 
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@RestController
@RequestMapping(value = "/v1/proxy")
@EnableConfigurationProperties(ProxyProperties.class)
public class ProxyController {

    @Autowired
    private ProxyProperties properties;



    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // NOTE: This proxy ignores all SSL incoming requests. This is great for POC's and prototyping, but not for production
    @RequestMapping(value = "/**")
    public ResponseEntity<String> authTest(HttpServletRequest request) throws java.net.URISyntaxException{

        if (properties.getUrl() == null) {
            throw new IllegalStateException("Proxy url not configured.");
        }

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

// Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

// Now you can access an https URL without having the certificate in the truststore
        RestTemplate restTemplate = new RestTemplate();

            // Calculate request url.
            final String baseUrl = properties.getUrl() + (request.getRequestURI() + "?" + request.getQueryString()).replace("/v1/proxy/","");
            logger.debug("Making http request to url: {}", baseUrl);
            URI uri = new URI(baseUrl);

            // Add a applicable auth header
            HttpHeaders headers = new HttpHeaders();
            if (properties.getAuthorization() != null) {
                headers.set("X-Api-Key", properties.getAuthorization());
            }
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

            return  restTemplate.exchange(uri, HttpMethod.valueOf(request.getMethod()), requestEntity, String.class);

    }
}
