package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.entity.Heartbeat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
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
@DataMongoTest
public class HeartbeatRepositoryIT {

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Test
    public void saveHeartbeat_findAll_returnsSavedEntityWithId() {
        // Given...
        Heartbeat heartbeat = new Heartbeat(
                NotificationTarget.of("foo"), LocalDateTime.of(2017, Month.JANUARY, 1, 12, 0));

        // When...
        heartbeatRepository.save(heartbeat);

        // Then...
        List<Heartbeat> heartbeats = heartbeatRepository.findAll();
        assertThat(heartbeats, hasSize(1));
        assertThat(heartbeats.get(0).getId(), not(isEmptyString()));
    }

    @Test
    public void oneMatchingHeartbeatOneNot_findFirstByNotificationTargetEqualsOrderByDateTimeDesc_returnsMatchingHeartbeat() {
        // Given...
        Heartbeat matchingHeartbeat = new Heartbeat(
                NotificationTarget.of("foo"), LocalDateTime.of(2017, Month.JANUARY, 1, 12, 0));
        Heartbeat nonMatchingHeartbeat = new Heartbeat(
                NotificationTarget.of("bar"), LocalDateTime.of(2017, Month.JANUARY, 1, 12, 0));

        // When...
        heartbeatRepository.saveAll(Arrays.asList(matchingHeartbeat, nonMatchingHeartbeat));

        // Then...
        Heartbeat heartbeat =
                heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(NotificationTarget.of("foo"));
        assertThat(heartbeat, is(notNullValue()));
        assertThat(heartbeat.getNotificationTarget().getTargetAddress(), is("foo"));
    }

    @Test
    public void twoMatchingHeartbeats_findFirstByNotificationTargetEqualsOrderByDateTimeDesc_returnsFirstHeartbeat() {
        // Given...
        NotificationTarget notificationTarget = NotificationTarget.of("foo");
        Heartbeat earlierHeartbeat = new Heartbeat(
                notificationTarget, LocalDateTime.of(2017, Month.JANUARY, 1, 12, 0));
        Heartbeat laterHeartbeat = new Heartbeat(
                notificationTarget, LocalDateTime.of(2017, Month.JANUARY, 1, 12, 1));

        // When...
        heartbeatRepository.saveAll(Arrays.asList(earlierHeartbeat, laterHeartbeat));

        // Then...
        Heartbeat heartbeat =
                heartbeatRepository.findFirstByNotificationTargetEqualsOrderByDateTimeDesc(notificationTarget);
        assertThat(heartbeat, is(notNullValue()));
        assertThat(heartbeat.getDateTime().getMinute(), is(1));
    }
}
