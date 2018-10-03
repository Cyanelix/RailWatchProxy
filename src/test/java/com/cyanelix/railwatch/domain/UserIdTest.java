package com.cyanelix.railwatch.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class UserIdTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void generateAUserId_get_isValidUUID() {
        // When...
        UserId userId = UserId.generate();

        // Then...
        assertThat(UUID.fromString(userId.get()), is(notNullValue()));
    }

    @Test
    public void invalidUUID_get_isNull() {
        // Given...
        expectedException.expect(IllegalArgumentException.class);

        String invalidUUID = "foo";

        // When...
        UserId.of(invalidUUID);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(UserId.class).verify();
    }

    @Test
    public void twoUserIdsWithSameUUID_equals_returnsTrue() {
        // Given...
        UserId userId1 = UserId.generate();
        UserId userId2 = UserId.of(userId1.get());

        // When...
        boolean equals = userId1.equals(userId2);

        // Then...
        assertThat(equals, is(true));
    }
}
