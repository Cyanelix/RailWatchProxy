package com.cyanelix.railwatch.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.*;
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

    @Test
    public void justMonday_containsMonday_returnsFalse() {
        // Given...
        DayRange justMonday = DayRange.of(MONDAY);

        // When...
        boolean contains = justMonday.contains(MONDAY);

        // Then...
        assertThat(contains, is(true));
    }

    @Test
    public void allButMonday_containsMonday_returnsFalse() {
        // Given...
        DayRange allButMonday = DayRange.of(TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY);

        // When...
        boolean contains = allButMonday.contains(MONDAY);

        // Then...
        assertThat(contains, is(false));
    }

    @Test
    public void allDays_testStoredInOrder() {
        // Given...
        DayRange all = DayRange.ALL;

        // When...
        Set<DayOfWeek> days = all.getDays();

        // Then...
        List<Integer> dayValues = days.stream().map(DayOfWeek::getValue).collect(Collectors.toList());
        assertThat(dayValues, is(Arrays.asList(
                DayOfWeek.MONDAY.getValue(),
                DayOfWeek.TUESDAY.getValue(),
                DayOfWeek.WEDNESDAY.getValue(),
                DayOfWeek.THURSDAY.getValue(),
                DayOfWeek.FRIDAY.getValue(),
                DayOfWeek.SATURDAY.getValue(),
                DayOfWeek.SUNDAY.getValue())));
    }

    @Test
    public void dayRangeCreatedOutOfOrder_testStoredInOrder() {
        // Given...
        DayRange range = DayRange.of(DayOfWeek.SUNDAY, DayOfWeek.TUESDAY);

        // When...
        Set<DayOfWeek> days = range.getDays();

        // Then...
        List<Integer> dayValues = days.stream().map(DayOfWeek::getValue).collect(Collectors.toList());
        assertThat(dayValues, is(Arrays.asList(DayOfWeek.TUESDAY.getValue(), DayOfWeek.SUNDAY.getValue())));
    }

    @Test
    public void allDays_toString_returnsAllDaysStringRepresentation() {
        // Given...
        DayRange range = DayRange.ALL;

        // When...
        String string = range.toString();

        // Then...
        assertThat(string, is("Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday"));
    }

    @Test
    public void arrayOfStrings_of_returnsCorrectValues() {
        // Given...
        String[] days = new String[] {"monday", "TUESDAY", "Wednesday", "THUrsday", "fRiday", "Saturday"};

        // When...
        DayRange dayRange = DayRange.of(days);

        // Then...
        assertThat(new ArrayList<>(dayRange.getDays()),
                is(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)));
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(DayRange.class).verify();
    }
}
