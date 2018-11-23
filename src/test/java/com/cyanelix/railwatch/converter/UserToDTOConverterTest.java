package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.dto.UserRequestResponse;
import com.cyanelix.railwatch.entity.User;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserToDTOConverterTest {
    @Test
    public void convertEntityToDTO_success() {
        // Given...
        UserId userId = UserId.generate();
        String notificationTarget = "notification-target";
        UserState userState = UserState.ENABLED;

        User user = new User(userId, notificationTarget, userState);

        // When...
        UserRequestResponse userRequestResponse = new UserEntityToDTOConverter().convert(user);

        // Then...
        assertThat(userRequestResponse.getUserId(), is(userId.get()));
        assertThat(userRequestResponse.getNotificationTarget(), is(notificationTarget));
    }
}
