package com.example.vehicles.vehicle;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Guarda o estado na BD como texto ("available"), igual ao que a API expõe. */
@Converter
public class VehicleStatusConverter implements AttributeConverter<VehicleStatus, String> {

    @Override
    public String convertToDatabaseColumn(VehicleStatus status) {
        return status == null ? null : status.getValue();
    }

    @Override
    public VehicleStatus convertToEntityAttribute(String value) {
        return value == null ? null : VehicleStatus.fromValue(value);
    }
}