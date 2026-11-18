package com.example.vehicles.vehicle;

/** O que a API devolve. É um record: imutável e sem boilerplate. Nunca expomos a entidade JPA. */
public record VehicleResponse(
        Long id,
        String plate,
        String brand,
        String model,
        int year,
        String status,
        int mileageKm) {

    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getPlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getStatus().getValue(),
                vehicle.getMileageKm());
    }
}