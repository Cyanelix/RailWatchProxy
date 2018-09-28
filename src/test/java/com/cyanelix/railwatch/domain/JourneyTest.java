package com.cyanelix.railwatch.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class JourneyTest {
    @Test
    public void testToString() {
        // Given...
        Journey journey = Journey.of(Station.of("FOO"), Station.of("BAR"));

        // When...
        String string = journey.toString();

        // Then...
        assertThat(string, is("FOO -> BAR"));
    }

    @Test(expected = NullPointerException.class)
    public void nullStations_of_throwsException() {
        // When...
        Journey.of(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void nullFrom_of_throwsException() {
        // When...
        Journey.of(null, Station.of("BAR"));
    }

    @Test(expected = NullPointerException.class)
    public void nullTo_of_throwsException() {
        // When...
        Journey.of(Station.of("FOO"), null);
    }

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(Journey.class).verify();
    }
}
