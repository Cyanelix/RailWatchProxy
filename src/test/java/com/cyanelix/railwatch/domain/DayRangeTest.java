package com.cyanelix.railwatch.domain;

import org.junit.Test;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DayRangeTest {
    @Test
    public void allDays_containsMonday_returnsTrue() {
        // Given...
        DayRange all = DayRange.ALL;

        // When...
        boolean contains = all.contains(MONDAY);

        // Then...
        assertThat(contains, is(true));
    }

    @Test
    public void justMonday_containsTuesday_returnsFalse() {
        // Given...
        DayRange justMonday = DayRange.of(MONDAY);

        // When...
        boolean contains = justMonday.contains(TUESDAY);

        // Then...
        assertThat(contains, is(false));
    }
}
