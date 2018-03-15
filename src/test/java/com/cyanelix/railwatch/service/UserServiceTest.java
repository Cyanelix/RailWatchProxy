package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.User;
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
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        assertThat(user.getNotificationTarget(), is(notificationTarget.getTargetAddress()));
        assertThat(user.getUserState(), is(UserState.ENABLED));

        ArgumentCaptor<User> userEntityCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userEntityCaptor.capture());

        User userEntity = userEntityCaptor.getValue();
        assertThat(userEntity.getUserId(), is(user.getUserId()));
        assertThat(userEntity.getNotificationTarget(), is(user.getNotificationTarget()));
        assertThat(userEntity.getUserState(), is(user.getUserState()));
    }

    @Test
    public void createUserWithNullNotificationTarget_throwsNullPointerException() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("A notification target is required for a User");

        userService.createUser(null);
    }

    @Test
    public void existingUser_getById_userReturned() {
        // Given...
        UserId userId = UserId.generate();
        User userEntity = new User(userId, "foo", UserState.ENABLED);

        when(userRepository.findByUserId(userId)).thenReturn(userEntity);

        // When...
        User user = userService.getUser(userId);

        // Then...
        assertThat(user.getUserId(), is(userId));
        assertThat(user.getNotificationTarget(), is("foo"));
        assertThat(user.getUserState(), is(UserState.ENABLED));
    }

    @Test
    public void disableUserByNotificationTarget_userStatusUpdated() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("target");
        User user = new User(UserId.generate(), notificationTarget.getTargetAddress(), UserState.ENABLED);

        when(userRepository.findByNotificationTarget(notificationTarget.getTargetAddress()))
                .thenReturn(user);

        // When...
        userService.disableUserByNotificationTarget(notificationTarget);

        // Then...
        verify(userRepository).save(user);
        assertThat(user.getUserState(), is(UserState.DISABLED));
    }

    @Test
    public void nonExistentUser_getUserById_returnsNull() {
        // Given...
        UserId userId = UserId.generate();
        when(userRepository.findByUserId(userId)).thenReturn(null);

        // When...
        User returnedUser = userService.getUser(userId);

        // Then...
        assertThat(returnedUser, is(nullValue()));
    }
}
