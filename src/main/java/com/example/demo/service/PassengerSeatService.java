package com.example.demo.service;

import com.example.demo.data.PassengerSeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerSeatService {

    @Autowired
    TicketService ticketService;

    @Autowired
    FlightService flightService;

    @Autowired
    ImageService imageService;

    @Autowired
    AirportService airportService;

    public PassengerSeat fill(PassengerSeat ps) {

        ps.setSurnameOptions(ticketService.listAllSurnames());

        if (ps.getSurname() == null || ps.getSurname().isEmpty()) {
            return ps;
        }

        ps.setNameVisible(true);
        ps.setNameOptions(ticketService.listNamesBySurname(ps.getSurname()));
        if (!ps.getNameOptions().contains(ps.getName())) {
            ps.setName(null);
        }

        if (ps.getNameOptions().size() == 1) {
            ps.setName(ps.getNameOptions().get(0));
        }

        if (ps.getName() == null || ps.getName().isEmpty()) {
            return ps;
        }

        ps.setImageDataVisible(true);
        ps.setImageData(imageService.searchImage(ps.getName() + " " + ps.getSurname()));

        ps.setIdVisible(true);
        ps.setIdOptions(ticketService.listIdsByPassengerName(ps.getName() + " " + ps.getSurname()));
        if (!ps.getIdOptions().contains(ps.getId())) {
            ps.setId(null);
        }

        if (ps.getIdOptions().size() == 1) {
            ps.setId(ps.getIdOptions().get(0));
        }

        if (ps.getId() == null || ps.getId().isEmpty()) {
            return ps;
        }

        ps.setAirportVisible(true);
        ps.setAirportOptions(airportService.getAirportsByPassenger(ps.getName() + " " + ps.getSurname(), ps.getId()));
        ps.setAirportInfo(airportService.getAirportInfos(ps.getAirportOptions()));

        if (!ps.getAirportOptions().contains(ps.getAirport())) {
            ps.setAirport(null);
        }

        if (ps.getAirportOptions().size() == 1) {
            ps.setAirport(ps.getAirportOptions().get(0));
        }

        if (ps.getAirport() == null || ps.getAirport().isEmpty()) {
            return ps;
        }

        ps.setDepartureVisible(true);
        ps.setDepartureOptions(flightService.getDeparturesByPassengerAndAirport(ps.getName() + " " + ps.getSurname(), ps.getId(), ps.getAirport()));
        if (!ps.getDepartureOptions().contains(ps.getDeparture())) {
            ps.setDeparture(null);
        }

        if (ps.getDeparture() == null || ps.getDeparture().isEmpty()) {
            return ps;
        }

        ps.setFlightInfoVisible(true);
        int flight = flightService.getFlightByPassengerAndAirportAndTime(ps.getName() + " " + ps.getSurname(), ps.getId(), ps.getAirport(), ps.getDeparture());
        String flightInfo = flightService.getFlightInfo(flight);
        String ticketInfo = ticketService.getTicketInfo(ps.getName() + " " + ps.getSurname(), ps.getId(), flight);
        ps.setFlightInfo(flightInfo + ticketInfo);

        return ps;
    }

}
