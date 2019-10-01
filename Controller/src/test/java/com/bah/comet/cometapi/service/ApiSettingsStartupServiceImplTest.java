package com.bah.comet.cometapi.service;

import com.bah.comet.cometapi.model.ApiSettings;
import com.bah.comet.cometapi.model.FeatureFlags;
import com.bah.comet.cometapi.model.User;
import com.bah.comet.cometapi.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiSettingsStartupServiceImplTest {

    /**
     * Mock the autowired dependencies
     */
    @Mock
    private ApiSettings apiSettingsMock;

    @Mock
    private UserRepository userRepositoryMock;

    /**
     * Inject the subject under test.
     */
    @InjectMocks
    private ApiSettingsStartupServiceImpl sut;

    /**
     * Other helpers
     */
    private List<User> nonDeletedPersonaUsers = new ArrayList<User>() {{
        add(new User(1, "user@email.com", "password", "joe", "smith", new Date(), "", null, null ));
        add(new User(2, "admin@email.com", "password1", "jane", "smith", new Date(), "", null, null ));
    }};

    private List<User> deletedPersonaUsers = new ArrayList<User>() {{
        add(new User(1, "user@email.com", "password", "joe", "smith", new Date(), "", new Date(), null ));
        add(new User(2, "admin@email.com", "password1", "jane", "smith", new Date(), "", new Date(), null ));
    }};

    private List<User> normalUsers = new ArrayList<User>() {{
        new User(1, "abd@email.com", "password123", "alex", "anderson", new Date(), "", null, null );
    }};

    private final List<String> PERSONAS = new ArrayList<String>() {{
        add("user@email.com");
        add("admin@email.com");
    }};

    @Before
    public void beforeEachTest() {
        // Persona emails
        when(apiSettingsMock.getPersonas()).thenReturn(PERSONAS);

        // Default to personas enabled.
        FeatureFlags featureFlags = new FeatureFlags();
        featureFlags.setPersonasEnabled(true);
        when(apiSettingsMock.getFeatureFlags()).thenReturn(featureFlags);
        List<User> users = Stream.concat(deletedPersonaUsers.stream(), normalUsers.stream()).collect(Collectors.toList());
        when(userRepositoryMock.findByEmailIn(anyList())).thenReturn(users);
    }

    @Test
    public void deletesPersonasIfFeatureFlagIsDisabled() {
        // Arrange
        FeatureFlags featureFlags = new FeatureFlags();
        featureFlags.setPersonasEnabled(false);
        when(apiSettingsMock.getFeatureFlags()).thenReturn(featureFlags);

        List<User> users = Stream.concat(nonDeletedPersonaUsers.stream(), normalUsers.stream()).collect(Collectors.toList());
        when(userRepositoryMock.findByEmailIn(anyList())).thenReturn(users);

        ArgumentCaptor<List<User>> usersArgument = ArgumentCaptor.forClass((new ArrayList<User>()).getClass());

        // Act
        sut.alignFeatureFlagData();

        // Assert
        verify(userRepositoryMock, times(1)).saveAll(usersArgument.capture());

        for(User u : usersArgument.getValue()) {
            assertNotNull(u.getDeleted());
        }
    }

    @Test
    public void resetsPersonasIfFeatureFlagIsEnabled() {
        // Arrange
        ArgumentCaptor<List<User>> usersArgument = ArgumentCaptor.forClass((new ArrayList<User>()).getClass());

        // Act
        sut.alignFeatureFlagData();

        // Assert
        verify(userRepositoryMock, times(1)).saveAll(usersArgument.capture());

        for(User u : usersArgument.getValue()) {
            assertNull(u.getDeleted());
        }
    }

}
