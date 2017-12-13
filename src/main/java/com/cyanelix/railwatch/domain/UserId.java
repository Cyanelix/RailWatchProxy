package com.cyanelix.railwatch.domain;

import java.util.UUID;

public final class UserId {
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
}
