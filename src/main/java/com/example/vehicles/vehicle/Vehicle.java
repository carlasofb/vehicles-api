package com.example.vehicles.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String plate;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String model;

    // "year" é palavra reservada em alguns SQL (H2), por isso a coluna chama-se manufacture_year
    @Column(name = "manufacture_year", nullable = false)
    private int year;

    @Convert(converter = VehicleStatusConverter.class)
    @Column(nullable = false, length = 20)
    private VehicleStatus status;

    @Column(name = "mileage_km", nullable = false)
    private int mileageKm;

    protected Vehicle() {
        // exigido pelo JPA
    }

    public Vehicle(String plate, String brand, String model, int year, VehicleStatus status, int mileageKm) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.status = status;
        this.mileageKm = mileageKm;
    }

    public Long getId() { return id; }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }

    public int getMileageKm() { return mileageKm; }
    public void setMileageKm(int mileageKm) { this.mileageKm = mileageKm; }
}