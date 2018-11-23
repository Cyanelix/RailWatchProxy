package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.dto.UserRequestResponse;
import com.cyanelix.railwatch.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDTOConverter implements Converter<User, UserRequestResponse> {
    @Override
    public UserRequestResponse convert(User user) {
        UserRequestResponse userRequestResponse = new UserRequestResponse();
        userRequestResponse.setUserId(user.getUserId().get());
        userRequestResponse.setNotificationTarget(user.getNotificationTarget());
        return userRequestResponse;
    }
}
