package com.cyanelix.railwatch.domain;

import org.junit.Test;
import nl.jqno.equalsverifier.*;

public class StationTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Station.class).verify();
    }
}
