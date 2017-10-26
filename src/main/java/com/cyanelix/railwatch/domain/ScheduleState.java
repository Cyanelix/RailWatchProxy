package com.cyanelix.railwatch.domain;

public enum ScheduleState {
    ENABLED, DISABLED;

    public static ScheduleState parse(String state) {
        if (state == null) {
            return ScheduleState.ENABLED;
        }

        return ScheduleState.valueOf(state.toUpperCase());
    }
}
