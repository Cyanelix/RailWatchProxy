package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.domain.UserId;
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

    @Test
    public void existingUser_getById_userReturned() {
        // Given...
        UserId userId = UserId.generate();
        UserEntity userEntity = new UserEntity(userId.get(), "foo", UserState.ENABLED);

        when(userRepository.findByUserId(userId.get())).thenReturn(userEntity);

        // When...
        UserEntity user = userService.getUser(userId);

        // Then...
        assertThat(user.getUserId(), is(userId.get()));
        assertThat(user.getNotificationTarget(), is("foo"));
        assertThat(user.getUserState(), is(UserState.ENABLED));
    }

    @Test
    public void disableUserByNotificationTarget_userStatusUpdated() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("target");
        UserEntity userEntity = new UserEntity(UserId.generate().get(), notificationTarget.getTargetAddress(), UserState.ENABLED);

        when(userRepository.findByNotificationTarget(notificationTarget.getTargetAddress()))
                .thenReturn(userEntity);

        // When...
        userService.disableUserByNotificationTarget(notificationTarget);

        // Then...
        verify(userRepository).save(userEntity);
        assertThat(userEntity.getUserState(), is(UserState.DISABLED));
    }

    @Test
    public void nonExistentUser_getUserById_returnsNull() {
        // Given...
        UserId userId = UserId.generate();
        when(userRepository.findByUserId(userId.get())).thenReturn(null);

        // When...
        UserEntity returnedUser = userService.getUser(userId);

        // Then...
        assertThat(returnedUser, is(nullValue()));
    }
}
