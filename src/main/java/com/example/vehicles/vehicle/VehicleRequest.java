package com.example.vehicles.vehicle;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** O que a API recebe em POST e PUT. As regras de validação estão junto aos campos. */
public record VehicleRequest(
        @NotBlank(message = "obrigatória")
        @Pattern(regexp = "^[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}$", message = "formato esperado: AA-12-BB")
        String plate,

        @NotBlank(message = "obrigatória") @Size(max = 50, message = "máximo 50 caracteres")
        String brand,

        @NotBlank(message = "obrigatório") @Size(max = 50, message = "máximo 50 caracteres")
        String model,

        @Min(value = 1990, message = "mínimo 1990") @Max(value = 2100, message = "máximo 2100")
        int year,

        @NotBlank(message = "obrigatório")
        @Pattern(regexp = "available|in_service|maintenance",
                 message = "valores permitidos: available, in_service, maintenance")
        String status,

        @Min(value = 0, message = "não pode ser negativo")
        int mileageKm) {
}