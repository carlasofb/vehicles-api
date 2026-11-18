package com.example.vehicles.vehicle;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(Long id) {
        super("Veículo " + id + " não encontrado");
    }
}