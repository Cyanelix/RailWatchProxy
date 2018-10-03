package com.cyanelix.railwatch.domain;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserStateTest {
    @Test
    public void nullString_valueOf_returnsEnabled() {
        // Given...
        String state = null;

        // When...
        UserState userState = UserState.parse(state);

        // Then...
        assertThat(userState, is(UserState.ENABLED));
    }

    @Test
    public void enabled_valueOf_returnsEnabled() {
        // Given...
        String state = "enabled";

        // When...
        UserState userState = UserState.parse(state);

        // Then...
        assertThat(userState, is(UserState.ENABLED));
    }

    @Test
    public void disabled_valueOf_returnsEnabled() {
        // Given...
        String state = "disabled";

        // When...
        UserState userState = UserState.parse(state);

        // Then...
        assertThat(userState, is(UserState.DISABLED));
    }
}
