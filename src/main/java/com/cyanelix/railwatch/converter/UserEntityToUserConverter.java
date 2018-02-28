package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.User;
import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.entity.UserEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToUserConverter implements Converter<UserEntity, User> {
    @Override
    public User convert(UserEntity userEntity) {
        return new User(
                UserId.of(userEntity.getUserId()),
                NotificationTarget.of(userEntity.getNotificationTarget()),
                userEntity.getUserState());
    }
}
