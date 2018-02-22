package com.example.demo.domain.air;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardingPassesRepository extends CrudRepository<BoardingPass, TicketFlightPK> {

    BoardingPass findByTicketNoAndFlightId(String ticketNo, int flightId);

    List<BoardingPass> findAllByFlightId(int flightId);

    List<BoardingPass> findAllByTicketNo(String ticketNo);
}
