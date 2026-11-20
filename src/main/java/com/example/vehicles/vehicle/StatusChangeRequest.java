package com.example.vehicles.vehicle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StatusChangeRequest(
        @NotBlank(message = "obrigatório")
        @Pattern(regexp = "available|in_service|maintenance",
                 message = "valores permitidos: available, in_service, maintenance")
        String status) {
}