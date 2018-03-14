package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.dto.UserDTO;
import com.cyanelix.railwatch.entity.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToDTOConverter implements Converter<UserEntity, UserDTO> {
    @Override
    public UserDTO convert(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setNotificationTarget(userEntity.getNotificationTarget());
        return userDTO;
    }
}
