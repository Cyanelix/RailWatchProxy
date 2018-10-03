package com.cyanelix.railwatch.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class UserId implements Serializable {
    private final UUID uuid;

    private UserId(UUID uuid) {
        this.uuid = uuid;
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(String uuid) {
        return new UserId(UUID.fromString(uuid));
    }

    public String get() {
        return uuid.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(uuid, userId.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
