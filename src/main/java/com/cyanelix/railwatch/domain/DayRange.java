package com.cyanelix.railwatch.domain;

import static java.time.DayOfWeek.*;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class DayRange {
    public static final DayRange ALL = new DayRange(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY);

    private final Set<DayOfWeek> days;

    private DayRange(DayOfWeek... days) {
        this.days = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(days)));
    }

    public static DayRange of(DayOfWeek day) {
        return new DayRange(day);
    }

    public boolean contains(DayOfWeek day) {
        return days.contains(day);
    }
}
