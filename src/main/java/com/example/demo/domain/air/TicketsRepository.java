package com.example.demo.domain.air;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketsRepository extends CrudRepository<Ticket, String> {

    Ticket findByTicketNo(String ticketNo);
}