package com.example.vehicles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.vehicles.vehicle.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VehicleStatusApiTest {

    private static final String BASE = "/api/v1/vehicles";

    @Autowired
    MockMvc mvc;

    @Autowired
    VehicleRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    private String createVehicle(String plate) throws Exception {
        String body = """
                {"plate":"%s","brand":"Renault","model":"Clio","year":2021,"status":"available","mileageKm":1000}
                """.formatted(plate);
        return mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(body))
                .andReturn().getResponse().getHeader("Location");
    }

    @Test
    void changeStatus_updatesOnlyTheStatus() throws Exception {
        String location = createVehicle("AA-12-BB");

        mvc.perform(patch(location + "/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"maintenance\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("maintenance"))
                .andExpect(jsonPath("$.plate").value("AA-12-BB"));
    }

    @Test
    void changeStatus_withInvalidValue_returns400() throws Exception {
        String location = createVehicle("CC-34-DD");

        mvc.perform(patch(location + "/status").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"parked\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.status").exists());
    }

    @Test
    void page_returnsSliceWithHasNext() throws Exception {
        createVehicle("AA-00-AA");
        createVehicle("BB-00-BB");
        createVehicle("CC-00-CC");

        mvc.perform(get(BASE + "/page").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.hasNext").value(true));

        mvc.perform(get(BASE + "/page").param("size", "2").param("page", "1"))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.hasNext").value(false));
    }
}