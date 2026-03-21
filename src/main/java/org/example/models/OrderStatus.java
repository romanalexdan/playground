package org.example.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    APPROVED("APPROVED"),
    PENDING("PENDING"),
    FAILED("FAILED");

    @JsonValue
    private final String value;
}
