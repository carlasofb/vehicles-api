package com.example.vehicles.vehicle;

import java.util.Arrays;

/** Estado de um veículo. O valor textual é o mesmo que o Angular usa ("in_service", etc.). */
public enum VehicleStatus {
    AVAILABLE("available"),
    IN_SERVICE("in_service"),
    MAINTENANCE("maintenance");

    private final String value;

    VehicleStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VehicleStatus fromValue(String value) {
        return Arrays.stream(values())
                .filter(status -> status.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado inválido: " + value));
    }
}