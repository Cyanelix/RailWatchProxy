package com.cyanelix.railwatch.domain;

public enum Formation {
    NORMAL(""), REVERSE("R"), UNSPECIFIED("");

    private String signifier;

    private Formation(String signifier) {
        this.signifier = signifier;
    }

    public String getSignifier() {
        return signifier;
    }
}
