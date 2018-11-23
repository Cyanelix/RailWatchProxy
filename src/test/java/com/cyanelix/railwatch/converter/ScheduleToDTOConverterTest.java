package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.dto.ScheduleRequestResponse;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScheduleToDTOConverterTest {
    @Test
    public void scheduleWithUser_convert_dtoPopulatedCorrectly() {
        // Given...
        User user = new User(UserId.generate(), NotificationTarget.of("foo").getTargetAddress(), UserState.ENABLED);

        Schedule schedule = new Schedule(
                LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL, Station.of("FOO"), Station.of("BAR"),
                ScheduleState.ENABLED, user);

        // When...
        ScheduleRequestResponse dto = new ScheduleToDTOConverter().convert(schedule);

        // Then...
        assertThat(dto.getStartTime(), is("12:00"));
        assertThat(dto.getEndTime(), is("00:00"));
        assertThat(dto.getFromStation(), is("FOO"));
        assertThat(dto.getToStation(), is("BAR"));
        assertThat(dto.getDays(), is(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}));
        assertThat(dto.getState(), is("ENABLED"));
        assertThat(dto.getUserId(), is(user.getUserId().get()));
    }

}
