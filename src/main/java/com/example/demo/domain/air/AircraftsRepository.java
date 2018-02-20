package com.example.demo.domain.air;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftsRepository extends CrudRepository<Aircraft, String> {

    Aircraft findByAircraftCode(String aircraftCode);
}