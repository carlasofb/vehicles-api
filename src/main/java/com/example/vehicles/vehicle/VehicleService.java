package com.example.vehicles.vehicle;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository repository;

    public VehicleService(VehicleRepository repository) {
        this.repository = repository;
    }

    public List<VehicleResponse> list(String q) {
        List<Vehicle> vehicles = (q == null || q.isBlank())
                ? repository.findAll(Sort.by("id"))
                : repository.search(q.trim());
        return vehicles.stream().map(VehicleResponse::from).toList();
    }

    public VehicleResponse get(Long id) {
        return repository.findById(id)
                .map(VehicleResponse::from)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }
}