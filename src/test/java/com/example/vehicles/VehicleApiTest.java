package com.example.vehicles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.vehicles.vehicle.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc   // necessário para injetar o MockMvc num @SpringBootTest
class VehicleApiTest {

    private static final String BASE = "/api/v1/vehicles";

    private static final String CLIO = """
            {"plate":"AA-12-BB","brand":"Renault","model":"Clio","year":2021,"status":"available","mileageKm":48200}
            """;

    @Autowired
    MockMvc mvc;

    @Autowired
    VehicleRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();   // cada teste parte de uma BD vazia
    }

    @Test
    void create_returns201_withLocationAndBody() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(CLIO))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.plate").value("AA-12-BB"))
                .andExpect(jsonPath("$.status").value("available"));
    }

    @Test
    void create_withInvalidData_returns400_withFieldErrors() throws Exception {
        String invalid = """
                {"plate":"aa-12-bb","brand":"","model":"Clio","year":1900,"status":"parked","mileageKm":-5}
                """;
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.plate").exists())
                .andExpect(jsonPath("$.errors.brand").exists())
                .andExpect(jsonPath("$.errors.year").exists())
                .andExpect(jsonPath("$.errors.status").exists())
                .andExpect(jsonPath("$.errors.mileageKm").exists());
    }

    @Test
    void create_withDuplicatePlate_returns409() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(CLIO))
                .andExpect(status().isCreated());
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(CLIO))
                .andExpect(status().isConflict());
    }

    @Test
    void get_unknownId_returns404() throws Exception {
        mvc.perform(get(BASE + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Veículo não encontrado"));
    }

    @Test
    void search_filtersByText() throws Exception {
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(CLIO));
        mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(
                CLIO.replace("AA-12-BB", "EE-56-FF").replace("Renault", "Volkswagen").replace("Clio", "Golf")));

        mvc.perform(get(BASE).param("q", "golf"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].model").value("Golf"));

        mvc.perform(get(BASE))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void update_and_delete() throws Exception {
        String location = mvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(CLIO))
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(put(location).contentType(MediaType.APPLICATION_JSON)
                        .content(CLIO.replace("48200", "50000")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mileageKm").value(50000));

        mvc.perform(delete(location)).andExpect(status().isNoContent());
        mvc.perform(get(location)).andExpect(status().isNotFound());
    }
}