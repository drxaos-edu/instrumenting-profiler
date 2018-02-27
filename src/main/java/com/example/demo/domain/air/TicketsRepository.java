package com.example.demo.domain.air;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketsRepository extends CrudRepository<Ticket, String> {

    Ticket findByTicketNo(String ticketNo);

    List<Ticket> findByPassengerNameLike(String search);

    @Query(name = "Ticket.findAllSurnames", value = "select split_part(t.passenger_name, ' ', 2) from tickets t group by split_part(t.passenger_name, ' ', 2) order by 1")
    List<String> findAllSurnames();
}