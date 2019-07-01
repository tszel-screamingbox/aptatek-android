package com.aptatek.pkulab.domain.manager.analytic;

public enum EventCategory {
    USER_BEHAVIOUR("User Behaviour"),
    ERROR("Error"),
    READER_COMMUNICATION("Reader Communication"),
    RISK_MITIGATION("Risk Mitigation");

    private final String key;

    EventCategory(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
