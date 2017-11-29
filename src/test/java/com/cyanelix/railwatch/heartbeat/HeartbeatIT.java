package com.cyanelix.railwatch.heartbeat;

import com.cyanelix.railwatch.domain.DayRange;
import com.cyanelix.railwatch.domain.NotificationTarget;
import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.entity.HeartbeatEntity;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.repository.HeartbeatRepository;
import com.cyanelix.railwatch.repository.ScheduleRepository;
import com.cyanelix.railwatch.service.HeartbeatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("it")
public class HeartbeatIT {
    @MockBean
    private Clock clock;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Autowired
    private HeartbeatService heartbeatService;

    @Before
    public void setup() {
        given(clock.instant()).willReturn(Instant.parse("2017-01-01T12:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());
    }

    @Test
    public void enabledAndDisabledSchedulesWithNotificationTargetsThatDoAndDoNotExpire() {
        // Given...
        ScheduleEntity expiringDisabled = createSchedule("ST1", "expiring", ScheduleState.DISABLED);
        ScheduleEntity expiringEnabled = createSchedule("ST2", "expiring", ScheduleState.ENABLED);
        ScheduleEntity notExpiringDisabled = createSchedule("ST3", "not-expiring", ScheduleState.DISABLED);
        ScheduleEntity notExpiringEnabled = createSchedule("ST4", "not-expiring", ScheduleState.ENABLED);
        ScheduleEntity noHeartbeatDisabled = createSchedule("ST5", "no-heartbeat", ScheduleState.DISABLED);
        ScheduleEntity noHeartbeatEnabled = createSchedule("ST5", "no-heartbeat", ScheduleState.ENABLED);
        ScheduleEntity warningDisabled = createSchedule("ST6", "warning", ScheduleState.DISABLED);
        ScheduleEntity warningEnabled = createSchedule("ST7", "warning", ScheduleState.ENABLED);
        scheduleRepository.save(Arrays.asList(expiringDisabled, expiringEnabled, notExpiringDisabled, notExpiringEnabled, noHeartbeatDisabled, noHeartbeatEnabled, warningDisabled, warningEnabled));

        HeartbeatEntity expiringHeartbeat = new HeartbeatEntity(NotificationTarget.of("expiring"), LocalDateTime.of(2016, 12, 22, 11, 59));
        HeartbeatEntity warningHeartbeat = new HeartbeatEntity(NotificationTarget.of("expiring"), LocalDateTime.of(2016, 12, 25, 11, 59));
        HeartbeatEntity notExpiringHeartbeat = new HeartbeatEntity(NotificationTarget.of("not-expiring"), LocalDateTime.of(2016, 12, 25, 12, 1));
        heartbeatRepository.save(Arrays.asList(expiringHeartbeat, notExpiringHeartbeat));

        // When...
        heartbeatService.checkHeartbeats();

        // Then...
        List<ScheduleEntity> expiringSchedules = scheduleRepository.findByNotificationTarget("expiring");
        expiringSchedules.forEach(scheduleEntity -> assertThat(scheduleEntity.getState(), is(ScheduleState.DISABLED)));

        ScheduleEntity retrievedNoHeartbeatDisabled = scheduleRepository.findOne(noHeartbeatDisabled.getId());
        assertThat(retrievedNoHeartbeatDisabled.getState(), is(ScheduleState.DISABLED));

        ScheduleEntity retrievedNoHeartbeatEnabled = scheduleRepository.findOne(noHeartbeatEnabled.getId());
        assertThat(retrievedNoHeartbeatEnabled.getState(), is(ScheduleState.ENABLED));

        ScheduleEntity retrievedNotExpiringDisabled = scheduleRepository.findOne(notExpiringDisabled.getId());
        assertThat(retrievedNotExpiringDisabled.getState(), is(ScheduleState.DISABLED));

        ScheduleEntity retrievedNotExpiringEnabled = scheduleRepository.findOne(notExpiringEnabled.getId());
        assertThat(retrievedNotExpiringEnabled.getState(), is(ScheduleState.ENABLED));

        ScheduleEntity retrievedWarningDisabled = scheduleRepository.findOne(warningDisabled.getId());
        assertThat(retrievedWarningDisabled.getState(), is(ScheduleState.DISABLED));

        ScheduleEntity retrievedWarningEnabled = scheduleRepository.findOne(warningEnabled.getId());
        assertThat(retrievedWarningEnabled.getState(), is(ScheduleState.ENABLED));
    }

    private ScheduleEntity createSchedule(String fromStation, String notificationTarget, ScheduleState scheduleState) {
        return new ScheduleEntity(LocalTime.MIN, LocalTime.MAX, DayRange.ALL, fromStation, "BAR", notificationTarget, scheduleState);
    }
}
