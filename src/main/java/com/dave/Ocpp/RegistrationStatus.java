package com.dave.Ocpp;

public enum RegistrationStatus {
    ACCEPTED("Accepted"),
    PENDING("Pending"),
    REJECTED("Rejected");

    private final String displayName;

    RegistrationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
