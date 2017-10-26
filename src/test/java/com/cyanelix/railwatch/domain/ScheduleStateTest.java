package com.cyanelix.railwatch.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ScheduleStateTest {
    @Test
    public void nullString_valueOf_returnsEnabled() {
        // Given...
        String state = null;

        // When...
        ScheduleState scheduleState = ScheduleState.parse(state);

        // Then...
        assertThat(scheduleState, is(ScheduleState.ENABLED));
    }

    @Test
    public void enabled_valueOf_returnsEnabled() {
        // Given...
        String state = "enabled";

        // When...
        ScheduleState scheduleState = ScheduleState.parse(state);

        // Then...
        assertThat(scheduleState, is(ScheduleState.ENABLED));
    }

    @Test
    public void disabled_valueOf_returnsEnabled() {
        // Given...
        String state = "disabled";

        // When...
        ScheduleState scheduleState = ScheduleState.parse(state);

        // Then...
        assertThat(scheduleState, is(ScheduleState.DISABLED));
    }
}
