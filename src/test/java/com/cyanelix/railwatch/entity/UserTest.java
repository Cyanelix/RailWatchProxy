package com.cyanelix.railwatch.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class UserTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(User.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("id")
                .verify();
    }
}
