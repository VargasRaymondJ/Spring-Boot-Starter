package com.bah.comet.cometapi.controller;

import com.bah.comet.cometapi.model.ApiSettings;
import com.bah.comet.cometapi.model.FeatureFlags;
import com.bah.comet.cometapi.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiSettingsControllerTest {

    /**
     * Mock the autowired dependencies
     */
    @Mock
    private ApiSettings apiSettingsMock;

    @InjectMocks
    private ApiSettingsController sut;

    @Before
    public void beforeEachTest() {
        // Default to personas enabled.
        FeatureFlags featureFlags = new FeatureFlags();
        featureFlags.setPersonasEnabled(true);
        when(apiSettingsMock.getFeatureFlags()).thenReturn(featureFlags);
    }

    @Test
    public void returnsApiSettings() {
        // Arrange

        // Act
        ApiSettings settings = sut.getSettings();

        // Assert
        assertEquals(apiSettingsMock, settings);
    }
}
