package com.cyanelix.railwatch.domain;

import static java.time.DayOfWeek.*;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DayRange {
    public static final DayRange ALL = DayRange.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY);

    private final SortedSet<DayOfWeek> days;

    private DayRange(Collection<DayOfWeek> days) {
        this.days = Collections.unmodifiableSortedSet(new TreeSet<>(days));
    }

    public static DayRange of(DayOfWeek... days) {
        return new DayRange(Arrays.asList(days));
    }

    public static DayRange of(Stream<DayOfWeek> days) {
        return new DayRange(days.collect(Collectors.toSet()));
    }

    public static DayRange of(String... days) {
        return DayRange.of(
                Stream.of(days)
                .map(String::toUpperCase)
                .map(DayOfWeek::valueOf));
    }

    public boolean contains(DayOfWeek day) {
        return days.contains(day);
    }

    public Set<DayOfWeek> getDays() {
        return days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayRange dayRange = (DayRange) o;
        return Objects.equals(days, dayRange.days);
    }

    @Override
    public int hashCode() {
        return Objects.hash(days);
    }

    @Override
    public String toString() {
        return days.stream()
                .map(dayOfWeek -> dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()))
                .collect(Collectors.joining(", "));
    }
}
