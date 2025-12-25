package com.raishxn.gticore.config;

import lombok.Getter;

@Getter
public enum AE2CalculationMode {

    LEGACY("legacy", "Using the original AE2 compositing algorithm"),
    FAST("fast", "Using a fast synthesis algorithm (which may lead to computational failure in extreme cases)."),
    ULTRA_FAST("ultra_fast", "An ultrafast synthesis algorithm is used (which avoids evenly distributing the results across different synthesis paths).");

    private final String name;
    private final String description;

    AE2CalculationMode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
