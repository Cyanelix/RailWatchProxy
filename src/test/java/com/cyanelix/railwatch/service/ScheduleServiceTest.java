package com.cyanelix.railwatch.service;

import com.cyanelix.railwatch.domain.Schedule;
import com.cyanelix.railwatch.domain.Station;
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
import java.time.LocalTime;
import java.time.ZoneId;

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

        Schedule activeSchedule = Schedule.of(LocalTime.MIN, LocalTime.MAX, null, null, null);
        scheduleService.createSchedule(activeSchedule);

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(mockTrainTimesService).lookupTrainTimes(any(), any());
        verify(mockNotificationService).sendNotification(eq(activeSchedule), any());
    }

    @Test
    public void createOneActiveOneInactiveSchedule_checkTimes_onlyActiveRouteLookedUp() {
        // Given...
        Schedule activeSchedule = Schedule.of(LocalTime.MIN, LocalTime.MAX, Station.of("FOO"), Station.of("BAR"), null);
        scheduleService.createSchedule(activeSchedule);

        Schedule inactiveSchedule = Schedule.of(LocalTime.MAX, LocalTime.MIN, Station.of("XXX"), Station.of("ZZZ"), null);
        scheduleService.createSchedule(inactiveSchedule);

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(mockTrainTimesService).lookupTrainTimes(Station.of("FOO"), Station.of("BAR"));
        verify(mockTrainTimesService, never()).lookupTrainTimes(Station.of("XXX"), Station.of("ZZZ"));

        verify(mockNotificationService).sendNotification(eq(activeSchedule), any());
        verify(mockNotificationService, never()).sendNotification(eq(inactiveSchedule), any());
    }

}
