package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.dto.UserDTO;
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
        UserDTO userDTO = new UserEntityToDTOConverter().convert(user);

        // Then...
        assertThat(userDTO.getUserId(), is(userId.get()));
        assertThat(userDTO.getNotificationTarget(), is(notificationTarget));
    }
}
