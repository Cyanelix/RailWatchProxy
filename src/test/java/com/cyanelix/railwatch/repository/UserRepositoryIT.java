package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.UserId;
import com.cyanelix.railwatch.domain.UserState;
import com.cyanelix.railwatch.entity.User;
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

        User user = new User(UserId.generate().get(), notificationTarget, UserState.ENABLED);
        userRepository.save(user);

        User duplicateUser = new User(UserId.generate().get(), notificationTarget, UserState.DISABLED);

        // When...
        userRepository.save(duplicateUser);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveUsersWithDuplicateUserIdsDifferentNotificationTargets_throwsException() {
        // Given...
        UserId userId = UserId.generate();

        User user = new User(userId.get(), "foo", UserState.ENABLED);
        userRepository.save(user);

        User duplicateUser = new User(userId.get(), "bar", UserState.DISABLED);

        // When...
        userRepository.save(duplicateUser);
    }

    @Test
    public void saveUsersWithUniqueUserIdsAndNotificationTargets_bothSavedSuccessfully() {
        // Given...
        User user = new User(UserId.generate().get(), "foo", UserState.ENABLED);
        userRepository.save(user);

        User duplicateUser = new User(UserId.generate().get(), "bar", UserState.ENABLED);

        // When...
        userRepository.save(duplicateUser);

        // Then...
        List<User> userEntities = userRepository.findAll();
        assertThat(userEntities, hasSize(2));
    }

    @Test
    public void nonExistentUser_findByUserId_returnsNull() {
        // When...
        User returnedUser = userRepository.findByUserId("foo");

        // Then...
        assertThat(returnedUser, is(nullValue()));
    }
}
