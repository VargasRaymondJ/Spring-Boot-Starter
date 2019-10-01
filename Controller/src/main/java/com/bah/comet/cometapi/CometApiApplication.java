package com.bah.comet.cometapi;

import com.bah.comet.cometapi.model.ApiSettings;
import com.bah.comet.cometapi.model.ProxyProperties;
import com.bah.comet.cometapi.service.ApiSettingsStartupService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CometApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CometApiApplication.class, args);
    }

    @Service
    static class Startup implements CommandLineRunner {

        @Autowired
        private ProxyProperties properties;

        @Autowired
        private ApiSettings settings;

        @Autowired
        private ApiSettingsStartupService apiSettingsStartupService;

        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void run(String... strings) {
            // Jackson mapper to print objects. Close the stream by default when done writing.
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

            // Print out application startup info.
            logger.info("-----------------------------------------");
            logger.info("-----Custom Configuration Properties-----");
            logger.info("-----------------------------------------");
            logger.info("Proxy URL: " + this.properties.getUrl());
            //logger.info("Proxy Auth header: " + this.properties.getAuthorization());
            logger.info("-----------------------------------------");
            try {
                logger.info("Api Settings: {}",mapper.writeValueAsString(settings));
            }
            catch (IOException e) {
                logger.error("Api Settings: Unable to print api settings.");
                logger.error(e.getMessage());
            }
            logger.info("-----------------------------------------");

            // Run startup services.
            apiSettingsStartupService.alignFeatureFlagData();
        }
    }

}
