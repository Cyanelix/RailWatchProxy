package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.dto.UserDTO;
import com.cyanelix.railwatch.entity.UserEntity;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserEntityToDTOConverterTest {
    @Test
    public void convertEntityToDTO_success() {
        // Given...
        UserId userId = UserId.generate();
        String notificationTarget = "notification-target";
        UserState userState = UserState.ENABLED;

        UserEntity userEntity = new UserEntity(userId.get(), notificationTarget, userState);

        // When...
        UserDTO userDTO = new UserEntityToDTOConverter().convert(userEntity);

        // Then...
        assertThat(userDTO.getUserId(), is(userId.get()));
        assertThat(userDTO.getNotificationTarget(), is(notificationTarget));
    }
}
