package com.example.vehicles.vehicle;

public class DuplicatePlateException extends RuntimeException {

    public DuplicatePlateException(String plate) {
        super("Já existe um veículo com a matrícula " + plate);
    }
}