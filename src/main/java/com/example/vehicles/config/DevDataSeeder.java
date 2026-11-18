package com.example.vehicles.config;

import com.example.vehicles.vehicle.Vehicle;
import com.example.vehicles.vehicle.VehicleRepository;
import com.example.vehicles.vehicle.VehicleStatus;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Dados de arranque para desenvolvimento (os mesmos 8 veículos do db.json). Sai no passo BE-3. */
@Component
public class DevDataSeeder implements CommandLineRunner {

    private final VehicleRepository repository;

    public DevDataSeeder(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        repository.saveAll(List.of(
                new Vehicle("AA-12-BB", "Renault", "Clio", 2021, VehicleStatus.AVAILABLE, 48200),
                new Vehicle("CC-34-DD", "Peugeot", "208", 2022, VehicleStatus.IN_SERVICE, 31500),
                new Vehicle("EE-56-FF", "Volkswagen", "Golf", 2019, VehicleStatus.MAINTENANCE, 96300),
                new Vehicle("GG-78-HH", "Toyota", "Corolla", 2023, VehicleStatus.AVAILABLE, 12400),
                new Vehicle("II-90-JJ", "Ford", "Transit", 2020, VehicleStatus.IN_SERVICE, 74800),
                new Vehicle("KK-11-LL", "Mercedes", "Sprinter", 2021, VehicleStatus.MAINTENANCE, 88100),
                new Vehicle("MM-22-NN", "Nissan", "Leaf", 2022, VehicleStatus.AVAILABLE, 25600),
                new Vehicle("OO-33-PP", "Citroën", "Berlingo", 2018, VehicleStatus.IN_SERVICE, 121000)));
    }
}