package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.domain.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.internal.matchers.Null;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class UsersServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UsersService usersService = new UsersService();

    @Test
    public void createUser_enabledUserReturnedWithIdAndNotificationTarget() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");

        // When...
        User user = usersService.createUser(notificationTarget);

        // Then...
        assertThat(user.getUserId(), is(notNullValue()));
        assertThat(user.getNotificationTarget(), is(notificationTarget));
        assertThat(user.getScheduleState(), is(ScheduleState.ENABLED));
    }

    @Test
    public void createUserWithNullNotificationTarget_throwsNullPointerException() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("A notification target is required for a User");

        usersService.createUser(null);
    }
}
