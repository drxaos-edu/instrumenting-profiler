package com.example.demo.service;

import com.example.demo.domain.air.FlightsRepository;
import com.example.demo.domain.air.TicketFlight;
import com.example.demo.domain.air.TicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

    @Autowired
    TicketsRepository ticketsRepository;

    @Autowired
    FlightsRepository flightsRepository;

    @Autowired
    TicketService ticketService;

    public List<Integer> getFlightsByPassenger(String fullName, String id) {
        List<String> tickets = ticketService.listTicketsByPassenger(fullName, id);

        List<Integer> flights = new ArrayList<>();
        for (String ticket : tickets) {
            for (TicketFlight ticketFlight : ticketsRepository.findByTicketNo(ticket).getTicketFlightsByTicketNo()) {
                flights.add(ticketFlight.getFlightId());
            }
        }
        flights.sort(Integer::compareTo);

        return flights;
    }

    public List<String> getDeparturesByPassengerAndAirport(String fullName, String id, String airportCode) {
        List<Integer> flights = getFlightsByPassenger(fullName, id);

        for (Integer flight : flights) {
            // TODO flightsRepository.findByFlightId(flight).getScheduledDeparture();
        }

        return null;
    }

    public String getFlightAirportCode(int flightId) {
        return flightsRepository.findByFlightId(flightId).getAirportsByDepartureAirport().getAirportCode();
    }

}
