package com.example.vehicles.events;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaTopicsConfig {

    @Bean
    public NewTopic vehicleStatusChangedTopic() {
        return TopicBuilder.name(VehicleEventsPublisher.TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}