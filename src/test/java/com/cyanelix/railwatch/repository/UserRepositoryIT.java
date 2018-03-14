package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        userRepository.deleteAll();
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveUsersWithDifferentUserIdsDuplicateNotificationTargets_throwsException() {
        // Given...
        String notificationTarget = "foo";

        UserEntity userEntity = new UserEntity(UserId.generate().get(), notificationTarget, UserState.ENABLED);
        userRepository.save(userEntity);

        UserEntity duplicateUserEntity = new UserEntity(UserId.generate().get(), notificationTarget, UserState.DISABLED);

        // When...
        userRepository.save(duplicateUserEntity);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveUsersWithDuplicateUserIdsDifferentNotificationTargets_throwsException() {
        // Given...
        UserId userId = UserId.generate();

        UserEntity userEntity = new UserEntity(userId.get(), "foo", UserState.ENABLED);
        userRepository.save(userEntity);

        UserEntity duplicateUserEntity = new UserEntity(userId.get(), "bar", UserState.DISABLED);

        // When...
        userRepository.save(duplicateUserEntity);
    }

    @Test
    public void saveUsersWithUniqueUserIdsAndNotificationTargets_throwsException() {
        // Given...
        UserEntity userEntity = new UserEntity(UserId.generate().get(), "foo", UserState.ENABLED);
        userRepository.save(userEntity);

        UserEntity duplicateUserEntity = new UserEntity(UserId.generate().get(), "bar", UserState.ENABLED);

        // When...
        userRepository.save(duplicateUserEntity);

        // Then...
        List<UserEntity> userEntities = userRepository.findAll();
        assertThat(userEntities, hasSize(2));
    }

    @Test
    public void nonExistentUser_findByUserId_returnsNull() {
        // When...
        UserEntity returnedUser = userRepository.findByUserId("foo");

        // Then...
        assertThat(returnedUser, is(nullValue()));
    }
}
