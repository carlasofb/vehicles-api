package com.example.vehicles;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.vehicles.vehicle.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VehiclesApiApplicationTests {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void seedsDevelopmentVehicles() {
		assertThat(vehicleRepository.count()).isEqualTo(8);
	}

}
