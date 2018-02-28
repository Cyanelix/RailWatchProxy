package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.entity.UserEntity;
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

public class ScheduleDTOToEntityConverterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserService userService;

    @InjectMocks
    private ScheduleDTOToEntityConverter converter;

    @Test
    public void dto_toSchedule() {
        // Given...
        UserId userId = UserId.generate();

        UserEntity user = createUser(userId, NotificationTarget.of("test"));
        given(userService.getUser(userId)).willReturn(user);

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setStartTime("12:00");
        scheduleDTO.setEndTime("00:00");
        scheduleDTO.setFromStation("FOO");
        scheduleDTO.setToStation("BAR");
        scheduleDTO.setUserId(userId.get());
        scheduleDTO.setDays(new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});

        // When...
        ScheduleEntity schedule = converter.convert(scheduleDTO);

        // Then...
        assertThat(schedule.getStartTime(), is(LocalTime.NOON));
        assertThat(schedule.getEndTime(), is(LocalTime.MIDNIGHT));
        assertThat(schedule.getFromStation(), is("FOO"));
        assertThat(schedule.getToStation(), is("BAR"));
        assertThat(schedule.getDayRange(), is(DayRange.ALL));
        assertThat(schedule.getUser().getUserId(), is(userId.get()));
        assertThat(schedule.getUser().getNotificationTarget(), is("test"));
    }

    // TODO: More coverage of error cases (what happens if user ID does not exist in DB?

    private UserEntity createUser(UserId userId, NotificationTarget notificationTarget) {
        return new UserEntity(userId.get(), notificationTarget.getTargetAddress(), UserState.ENABLED);
    }
}
