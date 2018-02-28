package com.cyanelix.railwatch.converter;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.dto.ScheduleDTO;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.entity.UserEntity;
import org.junit.Test;

import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScheduleEntityToDTOConverterTest {
    @Test
    public void scheduleWithUser_convert_dtoPopulatedCorrectly() {
        // Given...
        UserEntity user = new UserEntity(UserId.generate().get(), NotificationTarget.of("foo").getTargetAddress(), UserState.ENABLED);

        ScheduleEntity schedule = new ScheduleEntity(
                LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL, Station.of("FOO"), Station.of("BAR"),
                ScheduleState.ENABLED, "remove-notification-target", user);

        // When...
        ScheduleDTO dto = new ScheduleEntityToDTOConverter().convert(schedule);

        // Then...
        assertThat(dto.getStartTime(), is("12:00"));
        assertThat(dto.getEndTime(), is("00:00"));
        assertThat(dto.getFromStation(), is("FOO"));
        assertThat(dto.getToStation(), is("BAR"));
        assertThat(dto.getDays(), is(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"}));
        assertThat(dto.getState(), is("ENABLED"));
        assertThat(dto.getUserId(), is(user.getUserId()));
    }

}
