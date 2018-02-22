package com.example.demo.domain.air;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FlightsRepository extends CrudRepository<Flight, Integer> {

    Flight findByFlightId(Integer flightId);

    @Query(name = "Flight.findAllJoin", value = "SELECT c FROM Flight c join fetch c.ticketFlightsByFlightId")
    List<Flight> findAllJoin();

    @Query(name = "Flight.findAllBetweenDates", value = "SELECT c FROM Flight c where c.scheduledDeparture >= :fromTime and c.scheduledArrival <= :toTime")
    List<Flight> findAllBetweenDates(@Param("fromTime") Date from, @Param("toTime") Date to);
}
