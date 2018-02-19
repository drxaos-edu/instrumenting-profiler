package com.example.demo.domain.air;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportsRepository extends CrudRepository<AirportsEntity, Long> {

    AirportsEntity findByAirportCode(String airportCode);
}