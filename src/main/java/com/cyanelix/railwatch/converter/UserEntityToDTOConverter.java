package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.dto.UserDTO;
import com.cyanelix.railwatch.entity.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setNotificationTarget(user.getNotificationTarget());
        return userDTO;
    }
}
