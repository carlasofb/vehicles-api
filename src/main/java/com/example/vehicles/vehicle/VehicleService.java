package com.example.vehicles.vehicle;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)   // por defeito, só leitura; os métodos que escrevem sobrepõem com @Transactional
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

    @Transactional
    public VehicleResponse create(VehicleRequest request) {
        if (repository.existsByPlate(request.plate())) {
            throw new DuplicatePlateException(request.plate());
        }
        Vehicle vehicle = new Vehicle(
                request.plate(), request.brand(), request.model(), request.year(),
                VehicleStatus.fromValue(request.status()), request.mileageKm());
        return VehicleResponse.from(repository.save(vehicle));
    }

    @Transactional
    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = repository.findById(id).orElseThrow(() -> new VehicleNotFoundException(id));
        if (repository.existsByPlateAndIdNot(request.plate(), id)) {
            throw new DuplicatePlateException(request.plate());
        }
        vehicle.setPlate(request.plate());
        vehicle.setBrand(request.brand());
        vehicle.setModel(request.model());
        vehicle.setYear(request.year());
        vehicle.setStatus(VehicleStatus.fromValue(request.status()));
        vehicle.setMileageKm(request.mileageKm());
        // Sem save(): a entidade está "gerida" e o Hibernate grava as alterações no commit (dirty checking)
        return VehicleResponse.from(vehicle);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new VehicleNotFoundException(id);
        }
        repository.deleteById(id);
    }
}