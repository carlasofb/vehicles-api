package com.example.vehicles.events;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/** Só existe se app.kafka.enabled=true, para a aplicação correr sem Kafka em desenvolvimento. */
@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class VehicleEventsPublisher {

    public static final String TOPIC = "vehicle.status-changed";

    private final KafkaTemplate<String, VehicleStatusChanged> kafka;

    public VehicleEventsPublisher(KafkaTemplate<String, VehicleStatusChanged> kafka) {
        this.kafka = kafka;
    }

    // Corre DEPOIS do commit da transação que publicou o evento
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStatusChanged(VehicleStatusChanged event) {
        // chave = id do veículo → todos os eventos do mesmo veículo vão para a mesma partição (mantêm a ordem)
        kafka.send(TOPIC, String.valueOf(event.vehicleId()), event);
    }
}