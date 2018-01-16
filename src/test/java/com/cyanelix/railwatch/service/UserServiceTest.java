package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.UserEntity;
import com.cyanelix.railwatch.repository.UserRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class UserServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUser_enabledUserReturnedWithIdAndNotificationTarget() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");

        // When...
        User user = userService.createUser(notificationTarget);

        // Then...
        assertThat(user.getUserId(), is(notNullValue()));
        assertThat(user.getNotificationTarget(), is(notificationTarget));
        assertThat(user.getUserState(), is(UserState.ENABLED));

        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userEntityCaptor.capture());

        UserEntity userEntity = userEntityCaptor.getValue();
        assertThat(userEntity.getUserId(), is(user.getUserId().get()));
        assertThat(userEntity.getNotificationTarget(), is(user.getNotificationTarget().getTargetAddress()));
        assertThat(userEntity.getUserState(), is(user.getUserState()));
    }

    @Test
    public void createUserWithNullNotificationTarget_throwsNullPointerException() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("A notification target is required for a User");

        userService.createUser(null);
    }
}
