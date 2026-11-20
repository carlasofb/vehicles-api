package com.example.vehicles.events;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** Consumer de demonstração (num sistema real seria outro serviço, p.ex. "maintenance-service"). */
@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class MaintenanceListener {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceListener.class);

    // Idempotência: guarda os eventos já tratados (em produção seria uma tabela/Redis, não memória)
    private final Set<UUID> processed = ConcurrentHashMap.newKeySet();

    @KafkaListener(topics = VehicleEventsPublisher.TOPIC, groupId = "maintenance-service")
    public void onStatusChanged(VehicleStatusChanged event) {
        if (!processed.add(event.eventId())) {
            log.info("Evento duplicado ignorado: {}", event.eventId());
            return;
        }
        log.info("Veículo {} passou de '{}' para '{}'", event.plate(), event.from(), event.to());
    }
}