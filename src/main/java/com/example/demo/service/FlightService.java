package com.example.demo.service;

import com.example.demo.domain.air.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @Transactional
    public List<String> getDeparturesByPassengerAndAirport(String fullName, String id, String airportCode) {
        List<Integer> flights = getFlightsByPassenger(fullName, id);

        List<String> departures = new ArrayList<>();
        for (Integer flight : flights) {

            Airport airport = flightsRepository.findByFlightId(flight).getAirportsByDepartureAirport();
            if (!airport.getAirportCode().equals(airportCode)) {
                continue;
            }
            Timestamp scheduledDeparture = flightsRepository.findByFlightId(flight).getScheduledDeparture();
            String dt = formatTime(airport, scheduledDeparture);
            departures.add(dt);
        }

        return departures;
    }

    private String formatTime(Airport airport, Timestamp timestamp) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                        .withLocale(Locale.US)
                        .withZone(ZoneId.of(airport.getTimezone()));
        return formatter.format(timestamp.toInstant());
    }

    @Transactional
    public Integer getFlightByPassengerAndAirportAndTime(String fullName, String id, String airportCode, String time) {
        List<Integer> flights = getFlightsByPassenger(fullName, id);

        for (Integer flight : flights) {

            Airport airport = flightsRepository.findByFlightId(flight).getAirportsByDepartureAirport();
            if (!airport.getAirportCode().equals(airportCode)) {
                continue;
            }
            Timestamp scheduledDeparture = flightsRepository.findByFlightId(flight).getScheduledDeparture();
            String dt = formatTime(airport, scheduledDeparture);
            if (dt.equals(time)) {
                return flight;
            }
        }
        return null;
    }

    public String getFlightAirportCode(int flightId) {
        return flightsRepository.findByFlightId(flightId).getAirportsByDepartureAirport().getAirportCode();
    }

    @Transactional
    public String getFlightInfo(int flight) {
        Flight f = flightsRepository.findByFlightId(flight);
        return "Flight " + f.getFlightNo() +
                " (" + f.getAircraftsByAircraftCode().getModel() + ")" +
                "\n from " + f.getAirportsByDepartureAirport().getAirportCode() + " (" + f.getAirportsByDepartureAirport().getAirportName() + ")" +
                " at " + formatTime(f.getAirportsByDepartureAirport(), f.getScheduledDeparture()) +
                "\n to " + f.getAirportsByArrivalAirport().getAirportCode() + " (" + f.getAirportsByArrivalAirport().getAirportName() + ")" +
                " at " + formatTime(f.getAirportsByArrivalAirport(), f.getScheduledArrival()) +
                "\n status " + f.getStatus();

    }
}
