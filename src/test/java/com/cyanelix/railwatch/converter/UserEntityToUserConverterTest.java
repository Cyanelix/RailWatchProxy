package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.UserEntity;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserEntityToUserConverterTest {
    @Test
    public void validUserEntity_convert_validUser() {
        // Given...
        UserId userId = UserId.generate();
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        UserState userState = UserState.ENABLED;

        UserEntity entity = new UserEntity(userId.get(), notificationTarget.getTargetAddress(), userState);

        // When...
        User user = new UserEntityToUserConverter().convert(entity);

        // Then...
        assertThat(user.getUserId(), is(userId));
        assertThat(user.getNotificationTarget(), is(notificationTarget));
        assertThat(user.getUserState(), is(userState));
    }

    // TODO: more coverage here.
}
