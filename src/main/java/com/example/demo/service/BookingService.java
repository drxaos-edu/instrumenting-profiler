package com.example.demo.service;

import com.example.demo.domain.air.Booking;
import com.example.demo.domain.air.TicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    TicketsRepository ticketsRepository;

    public Booking getBookingByTicket(String ticketNo) {
        return ticketsRepository.findByTicketNo(ticketNo).getBookingsByBookRef();
    }

}
