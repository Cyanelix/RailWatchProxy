package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import com.cyanelix.railwatch.service.UserService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

public class ScheduleDTOToScheduleConverterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserService userService;

    @InjectMocks
    private ScheduleDTOToScheduleConverter converter;

    @Test
    public void dto_toSchedule() {
        // Given...
        UserId userId = UserId.generate();

        User user = createUser(userId, NotificationTarget.of("test"));
        given(userService.getUser(userId)).willReturn(user);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setStartTime("12:00");
        scheduleDTO.setEndTime("00:00");
        scheduleDTO.setFromStation("FOO");
        scheduleDTO.setToStation("BAR");
        scheduleDTO.setUserId(userId.get());
        scheduleDTO.setDays(new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});

        // When...
        Schedule schedule = converter.convert(scheduleDTO);

        // Then...
        assertThat(schedule.getStartTime(), is(LocalTime.NOON));
        assertThat(schedule.getEndTime(), is(LocalTime.MIDNIGHT));
        assertThat(schedule.getFromStation(), is(Station.of("FOO")));
        assertThat(schedule.getToStation(), is(Station.of("BAR")));
        assertThat(schedule.getDayRange(), is(DayRange.ALL));
    }

    private User createUser(UserId userId, NotificationTarget notificationTarget) {
        return new User(userId, notificationTarget.getTargetAddress(), UserState.ENABLED);
    }
}
