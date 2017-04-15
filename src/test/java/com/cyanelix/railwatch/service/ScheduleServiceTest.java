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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
    private NotificationService notificationService;

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void createSingleScheduleActiveNow_checkTimes_routeLookedUp() {
        // Given...
        createSchedule(LocalTime.MIN, LocalTime.MAX, Station.of("FOO"), Station.of("BAR"));

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(mockTrainTimesService).lookupTrainTimes(Station.of("FOO"), Station.of("BAR"));
        verify(notificationService).sendNotification(any(), any());
    }

    @Test
    public void createOneActiveOneInactiveSchedule_checkTimes_onlyActiveRouteLookedUp() {
        // Given...
        createSchedule(LocalTime.of(10, 0), LocalTime.of(11, 0), Station.of("ABC"), Station.of("DEF"));
        createSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0), Station.of("YYY"), Station.of("ZZZ"));

        // When...
        scheduleService.checkTimes();

        // Then...
        verify(mockTrainTimesService).lookupTrainTimes(Station.of("ABC"), Station.of("DEF"));
        verify(mockTrainTimesService, never()).lookupTrainTimes(Station.of("YYY"), Station.of("ZZZ"));
    }

    private void createSchedule(LocalTime min, LocalTime max, Station fromStation, Station toStation) {
        Schedule schedule = new Schedule();
        schedule.setStartTime(min);
        schedule.setEndTime(max);
        schedule.setFromStation(fromStation);
        schedule.setToStation(toStation);
        scheduleService.createSchedule(schedule);
    }
}
