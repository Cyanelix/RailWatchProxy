package com.cyanelix.railwatch.domain;

import java.util.Objects;

public final class NotificationTarget {
    private final String targetAddress;

    private NotificationTarget(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public static NotificationTarget of(String targetAddress) {
       return new NotificationTarget(targetAddress);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTarget that = (NotificationTarget) o;
        return Objects.equals(targetAddress, that.targetAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetAddress);
    }

    public String getTargetAddress() {
        return targetAddress;
    }
}
