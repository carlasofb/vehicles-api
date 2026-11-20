package com.example.vehicles.vehicle;

import com.example.vehicles.events.VehicleStatusChanged;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository repository;
    private final ApplicationEventPublisher events;

    public VehicleService(VehicleRepository repository, ApplicationEventPublisher events) {
        this.repository = repository;
        this.events = events;
    }

    public List<VehicleResponse> list(String q) {
        List<Vehicle> vehicles = (q == null || q.isBlank())
                ? repository.findAll(Sort.by("id"))
                : repository.search(q.trim());
        return vehicles.stream().map(VehicleResponse::from).toList();
    }

    public VehicleSlice page(String q, int page, int size) {
        Slice<Vehicle> slice = repository.searchSlice(q == null ? "" : q.trim(), PageRequest.of(page, size));
        List<VehicleResponse> content = slice.getContent().stream().map(VehicleResponse::from).toList();
        return new VehicleSlice(content, page, size, slice.hasNext());
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
        return VehicleResponse.from(vehicle);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new VehicleNotFoundException(id);
        }
        repository.deleteById(id);
    }

    /**
     * Muda o estado e publica um evento. O evento só segue para o Kafka DEPOIS do commit
     * (ver VehicleEventsPublisher), por isso nunca anunciamos uma mudança que acabou em rollback.
     */
    @Transactional
    public VehicleResponse changeStatus(Long id, String newStatus) {
        Vehicle vehicle = repository.findById(id).orElseThrow(() -> new VehicleNotFoundException(id));
        VehicleStatus target = VehicleStatus.fromValue(newStatus);
        VehicleStatus previous = vehicle.getStatus();

        if (previous != target) {
            vehicle.setStatus(target);
            events.publishEvent(new VehicleStatusChanged(
                    UUID.randomUUID(), vehicle.getId(), vehicle.getPlate(),
                    previous.getValue(), target.getValue(), Instant.now()));
        }
        return VehicleResponse.from(vehicle);
    }
}