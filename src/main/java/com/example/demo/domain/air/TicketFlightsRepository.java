package com.example.demo.domain.air;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketFlightsRepository extends CrudRepository<TicketFlight, TicketFlightPK> {

    TicketFlight findByTicketNoAndFlightId(String ticketNo, int flightId);

    List<TicketFlight> findAllByFlightId(int flightId);

    List<TicketFlight> findAllByTicketNo(String ticketNo);
}