package com.example.demo.domain.air;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatsRepository extends CrudRepository<Seat, SeatPK> {

    Seat findByAircraftCodeAndSeatNo(String aircraftCode, int seatNo);
}
