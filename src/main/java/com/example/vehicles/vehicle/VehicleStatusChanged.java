package com.example.vehicles.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento de domínio: "o estado de um veículo mudou". É imutável, por isso um record.
 * O eventId permite aos consumidores ignorar duplicados (o Kafka entrega "pelo menos uma vez").
 */
public record VehicleStatusChanged(
        UUID eventId,
        Long vehicleId,
        String plate,
        String from,
        String to,
        Instant occurredAt) {
}