package com.example.vehicles.vehicle;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}