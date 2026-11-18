package com.example.vehicles.vehicle;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    // GET /api/v1/vehicles?q=golf   → query param opcional (filtro)
    @GetMapping
    public List<VehicleResponse> list(@RequestParam(name = "q", required = false) String q) {
        return service.list(q);
    }

    // GET /api/v1/vehicles/3   → path variable (identifica o recurso)
    @GetMapping("/{id}")
    public VehicleResponse get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    // POST /api/v1/vehicles   → body JSON; responde 201 Created + header Location
    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        VehicleResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    // PUT /api/v1/vehicles/3   → substitui o recurso inteiro
    @PutMapping("/{id}")
    public VehicleResponse update(@PathVariable("id") Long id, @Valid @RequestBody VehicleRequest request) {
        return service.update(id, request);
    }

    // DELETE /api/v1/vehicles/3   → 204 No Content
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}