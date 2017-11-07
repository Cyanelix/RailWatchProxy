package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("it")
public class HeartbeatRepositoryIT {

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Test
    public void saveHeartbeat_findAll_returnsSavedEntityWithId() {
        // Given...
        HeartbeatEntity heartbeatEntity = new HeartbeatEntity(
                NotificationTarget.of("foo"), LocalDateTime.of(2017, Month.JANUARY, 1, 12, 0));

        // When...
        heartbeatRepository.save(heartbeatEntity);

        // Then...
        List<HeartbeatEntity> heartbeatEntities = heartbeatRepository.findAll();
        assertThat(heartbeatEntities, hasSize(1));
        assertThat(heartbeatEntities.get(0).getId(), not(isEmptyString()));
    }

    @Test
    public void oneMatchingHeartbeatOneNot_findFirstByNotificationTargetEqualsOrderByDateTimeDesc_returnsMatchingHeartbeat() {
        // Given...
        HeartbeatEntity matchingHeartbeat = new HeartbeatEntity(
                NotificationTarget.of("foo"), LocalDateTime.of(2017, Month.JANUARY, 1, 12, 0));
        HeartbeatEntity nonMatchingHeartbeat = new HeartbeatEntity(
                NotificationTarget.of("bar"), LocalDateTime.of(2017, Month.JANUARY, 1, 12, 0));

        // When...
        heartbeatRepository.save(Arrays.asList(matchingHeartbeat, nonMatchingHeartbeat));

        // Then...
        HeartbeatEntity heartbeatEntity =
                heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(NotificationTarget.of("foo"));
        assertThat(heartbeatEntity, is(notNullValue()));
        assertThat(heartbeatEntity.getNotificationTarget().getTargetAddress(), is("foo"));
    }

    @Test
    public void twoMatchingHeartbeats_findFirstByNotificationTargetEqualsOrderByDateTimeDesc_returnsFirstHeartbeat() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        HeartbeatEntity earlierHeartbeat = new HeartbeatEntity(
                notificationTarget, LocalDateTime.of(2017, Month.JANUARY, 1, 12, 0));
        HeartbeatEntity laterHeartbeat = new HeartbeatEntity(
                notificationTarget, LocalDateTime.of(2017, Month.JANUARY, 1, 12, 1));

        // When...
        heartbeatRepository.save(Arrays.asList(earlierHeartbeat, laterHeartbeat));

        // Then...
        HeartbeatEntity heartbeatEntity =
                heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget);
        assertThat(heartbeatEntity, is(notNullValue()));
        assertThat(heartbeatEntity.getDateTime().getMinute(), is(1));
    }
}
