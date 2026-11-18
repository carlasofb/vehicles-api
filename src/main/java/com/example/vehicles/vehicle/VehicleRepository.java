package com.example.vehicles.vehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByPlate(String plate);

    boolean existsByPlateAndIdNot(String plate, Long id);

    // Pesquisa por texto na matrícula, marca ou modelo (o ?q= do Angular)
    @Query("""
            select v from Vehicle v
            where lower(v.plate) like lower(concat('%', :q, '%'))
               or lower(v.brand) like lower(concat('%', :q, '%'))
               or lower(v.model) like lower(concat('%', :q, '%'))
            order by v.id
            """)
    List<Vehicle> search(@Param("q") String q);
}