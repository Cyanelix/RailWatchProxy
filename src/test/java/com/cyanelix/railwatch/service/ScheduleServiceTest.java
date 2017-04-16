package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleServiceTest {
    @TestConfiguration
    public static class TestApplicationConfiguration {
        @Bean
        public Clock clock() {
            return Clock.fixed(Instant.parse("2017-01-01T10:30:00Z"), ZoneId.systemDefault());
        }
    }

    @MockBean
    private TrainTimesService mockTrainTimesService;

    @MockBean
    private NotificationService mockNotificationService;

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void createSingleScheduleActiveNow_checkTimes_routeLookedUp() {
        // Given...
        Schedule activeSchedule = createSchedule(true);

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(activeSchedule).lookupAndNotifyTrainTimes(mockTrainTimesService, mockNotificationService);
    }

    @Test
    public void createOneActiveOneInactiveSchedule_checkTimes_onlyActiveRouteLookedUp() {
        // Given...
        Schedule activeSchedule = createSchedule(true);
        Schedule inactiveSchedule = createSchedule(false);

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(activeSchedule).lookupAndNotifyTrainTimes(mockTrainTimesService, mockNotificationService);
        verify(inactiveSchedule, never()).lookupAndNotifyTrainTimes(mockTrainTimesService, mockNotificationService);
    }

    private Schedule createSchedule(boolean isActive) {
        Schedule schedule = mock(Schedule.class);
        given(schedule.isActive(any())).willReturn(isActive);
        scheduleService.createSchedule(schedule);
        return schedule;
    }
}
