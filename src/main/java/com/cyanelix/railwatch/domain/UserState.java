package com.cyanelix.railwatch.domain;

public enum UserState {
    ENABLED, DISABLED;

    public static UserState parse(String state) {
        if (state == null) {
            return UserState.ENABLED;
        }

        return UserState.valueOf(state.toUpperCase());
    }
}
